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
        // rooms:{roomId}:users 삭제
        Set<String> userListKeys = redisTemplate.keys(
                RoomRedisKeyUtils.ROOM_PREFIX + "*" + RoomRedisKeyUtils.INSIDE_USERS_SUFFIX);
        if (userListKeys != null && !userListKeys.isEmpty()) {
            redisTemplate.delete(userListKeys);
        }
        
        // user:{sessionId}:room 삭제
        Set<String> userRoomKeys = redisTemplate.keys(
                RoomRedisKeyUtils.USER_PREFIX + "*" + RoomRedisKeyUtils.ROOM_SUFFIX);
        if(userRoomKeys != null && !userRoomKeys.isEmpty()){
            redisTemplate.delete(userRoomKeys);
        }

        // rooms:{roomId}:current 삭제
        Set<String> currentSongKeys = redisTemplate.keys(
                RoomRedisKeyUtils.ROOM_PREFIX + "*" + RoomRedisKeyUtils.CURRENT_SONG_SUFFIX);
        if (currentSongKeys != null && !currentSongKeys.isEmpty()) {
            redisTemplate.delete(currentSongKeys);
        }

        log.info("server restart - user and song info delete");
    }
}

