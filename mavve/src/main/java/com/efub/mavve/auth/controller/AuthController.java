package com.efub.mavve.auth.controller;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.dto.request.KakaoCodeRequest;
import com.efub.mavve.auth.service.AuthService;
import com.efub.mavve.auth.service.jwt.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> loginOrRegister(@RequestBody @Valid KakaoCodeRequest request, HttpServletResponse response) {
        authService.loginOrRegisterUser(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        authService.reissue(refreshToken, response);
        return ResponseEntity.created(null).build();
    }
}
