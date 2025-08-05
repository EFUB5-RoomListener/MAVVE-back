package com.efub.mavve.auth.controller;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.dto.request.SpotifyCodeRequest;
import com.efub.mavve.auth.dto.request.UserInfoRequest;
import com.efub.mavve.auth.dto.response.SpotifyAccessTokenResponse;
import com.efub.mavve.auth.dto.response.SpotifyRedirctUri;
import com.efub.mavve.auth.dto.response.UserInfoResponse;
import com.efub.mavve.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> loginOrRegister(@RequestBody @Valid SpotifyCodeRequest request,
                                                HttpServletResponse response) {
        authService.loginOrRegisterUser(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal User user,
                                       HttpServletResponse response,
                                       @RequestHeader("Authorization") String accessToken) {
        authService.logout(user, accessToken, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/redirect/spotify")
    public ResponseEntity<SpotifyRedirctUri> redirectToSpotify() {
        return ResponseEntity.ok(authService.getRedirctUri());
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        authService.reissue(refreshToken, response);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.getUserInfo(user));
    }

    @PatchMapping("/users/me")
    public ResponseEntity<UserInfoResponse> updateUserInfo(@AuthenticationPrincipal User user,
                                                           @Valid @RequestBody UserInfoRequest userInfoRequest){
        return ResponseEntity.ok(authService.updateUserInfo(user, userInfoRequest));
    }

    @GetMapping("/spotify-token")
    public ResponseEntity<SpotifyAccessTokenResponse> getSpotifyAccessToken(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(authService.getSpotifyAccessToken(user));
    }
}
