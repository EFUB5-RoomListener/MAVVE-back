package com.efub.mavve.auth.service.jwt;

import com.efub.mavve.global.exception.CustomAuthenticationException;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtResolver {

    private final JwtProperties jwtProperties;

    public String resolveAccessToken(String token) {
        return resolveTokenByType(token, "accessToken");
    }

    public String resolveRefreshToken(String token) {
        return resolveTokenByType(token, "refreshToken");
    }

    public String resolveTokenByType(String token, String type) {
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            validateTokenByType(claims, type);

            Long userId = Long.parseLong(claims.getSubject());
            return Long.toString(userId);
        }catch (ExpiredJwtException exception) {
            throw new CustomAuthenticationException(ExceptionCode.AUTH_TOKEN_EXPIRED, ExceptionCode.AUTH_TOKEN_EXPIRED.getMessage(), exception);
        } catch (JwtException exception) {
            throw new CustomAuthenticationException(ExceptionCode.AUTH_TOKEN_INVALID, ExceptionCode.AUTH_TOKEN_INVALID.getMessage(), exception);
        }
    }


    public void validateTokenByType(Claims claims, String type){
        String extractTokenType = claims.get("type", String.class);
        if (extractTokenType == null || !extractTokenType.equals(type)) {
            throw new MavveException(ExceptionCode.AUTH_TOKEN_MISMATCH);
        }
    }
}
