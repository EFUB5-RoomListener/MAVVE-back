package com.efub.mavve.auth.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.dto.request.SpotifyCodeRequest;
import com.efub.mavve.auth.dto.request.UserInfoRequest;
import com.efub.mavve.auth.dto.response.SpotifyRedirctUri;
import com.efub.mavve.auth.dto.response.SpotifyUserInfoResponse;
import com.efub.mavve.auth.dto.response.UserInfoResponse;
import com.efub.mavve.auth.repository.UserRepository;
import com.efub.mavve.auth.service.jwt.JwtProvider;
import com.efub.mavve.auth.service.jwt.JwtResolver;
import com.efub.mavve.auth.service.jwt.TokenService;
import com.efub.mavve.auth.service.jwt.SpotifyTokenService;
import com.efub.mavve.auth.service.oauth.OauthClient;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final TokenService refreshTokenService;
    private final SpotifyTokenService  spotifyTokenService;
    private final OauthClient oauthClient;

    private final long REFRESH_EXPIRE = 1000L * 60 * 60 * 24 * 14;
    private final JwtResolver jwtResolver;
    private final TokenService tokenService;

    public SpotifyRedirctUri getRedirctUri() {
        return SpotifyRedirctUri.of(oauthClient.getRedirectUri());
    }

    @Transactional
    public void loginOrRegisterUser(SpotifyCodeRequest request, HttpServletResponse response){
        String code = request.getCode();
        SpotifyUserInfoResponse userInfoResponse = oauthClient.requestOauthInfo(code);

        if(existedBySpotifyUserId(userInfoResponse.getId())){
            // 존재하는 유저이므로 로그인
            spotifyLogin(userInfoResponse, response);
            return;
        }
        // 존재하지 않는 유저이므로 회원가입
        registerKakaoUser(userInfoResponse, response);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(User user) {
        return UserInfoResponse.fromUserEntity(user);
    }

    @Transactional
    public UserInfoResponse updateUserInfo(User user, UserInfoRequest userInfoRequest) {
        User updateUser = getUserByUserId(user.getUserId());
        String newUsername = userInfoRequest.getNickname();
        String newProfileImageUrl = userInfoRequest.getProfile();
        updateUser.updateProfile(newUsername, newProfileImageUrl);
        return UserInfoResponse.fromUserEntity(updateUser);
    }

    @Transactional
    public void logout(User user, String accessToken, HttpServletResponse response, String refreshToken) {
        refreshTokenService.deleteRefreshToken(user.getUserId().toString());
        String originAccessToken = accessToken.substring(7);
        long remainingTime = jwtResolver.getRemainingTime(originAccessToken);
        tokenService.registerBlackList(originAccessToken, remainingTime);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @Transactional
    public void reissue(String oldRefreshToken, HttpServletResponse response){
        if(oldRefreshToken == null){
            throw new MavveException(ExceptionCode.REFRESH_TOKEN_EMPTY);
        }

        // 리프레시 토큰에 있는 user id 가져오기
        String userId = jwtResolver.resolveRefreshToken(oldRefreshToken);
        // redis에 해당 리프레시 토큰이 없으면 예외 처리
        if(!refreshTokenService.existsRefreshToken(userId, oldRefreshToken)){
            throw new MavveException(ExceptionCode.AUTH_TOKEN_INVALID);
        }
        User user = getUserByUserId(Long.valueOf(userId));

        // accessToken과 refreshToken 재 발급
        sendTokens(user, response);
    }


    private void spotifyLogin(SpotifyUserInfoResponse userInfoResponse, HttpServletResponse response){
        String spotifyUserId = userInfoResponse.getId();
        User user = userRepository.findBySpotifyUserId(spotifyUserId)
                .orElseThrow(()-> new MavveException(ExceptionCode.USER_NOT_FOUND));
        log.info("로그인한 유저 : {}", user.getUsername());
        sendTokens(user, response); // 우리 서비스 기반 토큰 발급 및 저장
        saveSpotifyTokens(user.getUserId(), userInfoResponse);
    }

    private void registerKakaoUser(SpotifyUserInfoResponse userInfoResponse, HttpServletResponse response){
        String username = userInfoResponse.getDisplay_name();
        String spotifyUserId = userInfoResponse.getId();
        User user = User.fromSpotifyInfo(username, spotifyUserId);
        log.info("회원가입한 유저: {}", username);
        userRepository.save(user);
        sendTokens(user, response);
        saveSpotifyTokens(user.getUserId(), userInfoResponse);
    }

    private void saveSpotifyTokens(Long userId, SpotifyUserInfoResponse spotifyUserInfoResponse){
        String spotifyAccessToken = spotifyUserInfoResponse.getToken().getAccess_token();
        String spotifyRefreshToken = spotifyUserInfoResponse.getToken().getRefresh_token();
        String userIdStr = Long.toString(userId);
        spotifyTokenService.saveAccessToken(userIdStr, spotifyAccessToken);
        spotifyTokenService.saveRefreshToken(userIdStr, spotifyRefreshToken);
    }

    // 액세스토큰과 리프레시 토큰 생성 후 각각 헤더와 쿠키에 담아 보냄
    private void sendTokens(User user, HttpServletResponse response){
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        refreshTokenService.deleteRefreshToken(user.getUserId().toString());
        refreshTokenService.saveRefreshToken(user.getUserId().toString(), refreshToken, REFRESH_EXPIRE);

        Cookie cookie = saveRefreshTokenCookie(refreshToken);
        response.addCookie(cookie);
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    private Cookie saveRefreshTokenCookie(String refreshToken){
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge((int) (REFRESH_EXPIRE / 1000));
        return refreshTokenCookie;
    }

    private boolean existedBySpotifyUserId(String spotifyUserId){
        return userRepository.existsBySpotifyUserId(spotifyUserId);
    }

    private User getUserByUserId(Long userId){
        return userRepository.findByUserId(userId).orElseThrow(()->new MavveException(ExceptionCode.USER_NOT_FOUND));
    }

}
