package com.efub.mavve.auth.service.jwt;

import com.efub.mavve.auth.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String createAccessToken(User user) {
        long accessTokenExpirationMills = jwtProperties.getAccessTokenExpirationMills();
        return createToken(user, accessTokenExpirationMills, "accessToken");

    }

    public String createRefreshToken(User user) {
        long refreshTokenExpirationMills = jwtProperties.getRefreshTokenExpirationMills();
        return createToken(user, refreshTokenExpirationMills, "refreshToken");
    }

    public String createToken(User user, long expiration, String tokenType){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .claim("type", tokenType)
                .signWith(jwtProperties.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
