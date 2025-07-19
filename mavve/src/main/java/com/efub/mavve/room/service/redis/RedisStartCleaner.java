package com.efub.mavve.room.service.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisStartCleaner {

    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    public void cleanUpUsersAndCurrentSong() {
        // users 키 삭제
        Set<String> userKeys = redisTemplate.keys(RoomRedisKeyUtils.ROOM_PREFIX + "*"
                + RoomRedisKeyUtils.INSIDE_USERS_SUFFIX);
        if (userKeys != null && !userKeys.isEmpty()) {
            redisTemplate.delete(userKeys);
        }

        // current song 키 삭제
        Set<String> currentSongKeys = redisTemplate.keys(RoomRedisKeyUtils.ROOM_PREFIX + "*"
                + RoomRedisKeyUtils.CURRENT_SONG_SUFFIX);
        if (currentSongKeys != null && !currentSongKeys.isEmpty()) {
            redisTemplate.delete(currentSongKeys);
        }

        log.info("server restart - user and song info delete");
    }
}

