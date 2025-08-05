package com.efub.mavve.auth.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SpotifyTokenService {
    private static final String ACCESSPRIFIX = "spotify:access:";
    private static final String REFRESHPRIFIX = "spotify:refresh:";
    private final RedisTemplate<String, String> redisTemplate;

    public void saveAccessToken(String userId, String accessToken) {
        redisTemplate.opsForValue()
                .set(ACCESSPRIFIX + userId, accessToken, Duration.ofSeconds(3600));
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue()
                .set(REFRESHPRIFIX + userId, refreshToken);
    }

    public String getAccessToken(String userId) {
        return redisTemplate.opsForValue().get(ACCESSPRIFIX + userId);
    }

    public Long getTtl(String userId) {return redisTemplate.getExpire(ACCESSPRIFIX + userId, TimeUnit.SECONDS);}

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(REFRESHPRIFIX + userId);
    }

    public void deleteAccessToken(String userId) {
        redisTemplate.delete(ACCESSPRIFIX + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(REFRESHPRIFIX + userId);
    }

}
