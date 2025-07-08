package com.efub.mavve.auth.service;

import com.efub.mavve.MavveApplication;
import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.dto.request.KakaoCodeRequest;
import com.efub.mavve.auth.dto.response.KakaoUserInfoResponse;
import com.efub.mavve.auth.repository.UserRepository;
import com.efub.mavve.auth.service.jwt.JwtProvider;
import com.efub.mavve.auth.service.jwt.JwtResolver;
import com.efub.mavve.auth.service.jwt.RefreshTokenService;
import com.efub.mavve.auth.service.oauth.OauthClient;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    private final RefreshTokenService refreshTokenService;
    private final OauthClient oauthClient;

    private final long REFRESH_EXPIRE = 1000L * 60 * 60 * 24 * 14;
    private final JwtResolver jwtResolver;

    @Transactional
    public void loginOrRegisterUser(KakaoCodeRequest request, HttpServletResponse response){
        String code = request.getCode();

        KakaoUserInfoResponse userInfoResponse = oauthClient.requestOauthInfo(code);
        Long kakaoId = userInfoResponse.getId();


        if(isExistedUser(kakaoId)){
            // 존재하는 유저이므로 로그인
            kakaoLogin(kakaoId, response);
            return;
        }
        // 존재하지 않는 유저이므로 회원가입
        registerKakaoUser(kakaoId, userInfoResponse, response);
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


    private void kakaoLogin(Long kakaoId, HttpServletResponse response){
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(()-> new MavveException(ExceptionCode.USER_NOT_FOUND));
        log.info("로그인한 유저 : {}", user.getUsername());
        sendTokens(user, response);

    }

    private void registerKakaoUser(Long kakaoId, KakaoUserInfoResponse userInfoResponse, HttpServletResponse response){
        String username = userInfoResponse.getKakao_account().getProfile().getNickname();
        User user = User.fromKakaoInfo(username, kakaoId);
        log.info("회원가입한 유저: {}", username);
        userRepository.save(user);
        sendTokens(user, response);
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

    private boolean isExistedUser(Long kakaoId) {
        return userRepository.existsByKakaoId(kakaoId);
    }

    private User getUserByUserId(Long userId){
        return userRepository.findByUserId(userId).orElseThrow(()->new MavveException(ExceptionCode.USER_NOT_FOUND));
    }

}
