package com.efub.mavve.auth.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final String REFRESHPRIFIX = "RT";
    private final StringRedisTemplate stringRedisTemplate;

    public void saveRefreshToken(String userId, String refreshToken, long durationMillis) {
        stringRedisTemplate.opsForValue()
                .set(REFRESHPRIFIX + userId, refreshToken, Duration.ofMillis(durationMillis));
    }

    public String getRefreshToken(String userId){
        return stringRedisTemplate.opsForValue().get(REFRESHPRIFIX + userId);
    }

    public void deleteRefreshToken(String userId){
        stringRedisTemplate.delete(REFRESHPRIFIX + userId);
    }

    public boolean existsRefreshToken(String userId, String refreshToken) {
        String storedRefreshToken = getRefreshToken(userId);
        return storedRefreshToken != null & refreshToken.equals(storedRefreshToken);
    }
}
