package com.efub.mavve.auth.service.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
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

    public void registerBlackList(String accessToken, long expiration){
        log.info("Registering token in blacklist: {} with TTL: {}ms", accessToken, expiration);
        stringRedisTemplate.opsForValue()
                .set("blacklist:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }
}
