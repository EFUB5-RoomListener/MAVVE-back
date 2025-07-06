package com.efub.mavve.auth.service.jwt;


import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Getter
public class JwtProperties {
    private final String secretKey;
    private final long accessTokenExpirationMills = 1000 * 60 * 60; // 1시간으로 설정, 개발 막바지에 15분으로 변경 예정
    private final long refreshTokenExpirationMills = 1000 * 60 * 60 * 24 * 14; // 2주

    public JwtProperties(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

}
