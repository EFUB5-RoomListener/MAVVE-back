package com.efub.mavve.room.service;

import com.efub.mavve.room.dto.summary.SongSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomRedisService {

    private final RedisTemplate<String, Object> objectRedisTemplate;

    private static final String SONGLIST_PREFIX = "room:";

    // 노래 추가
    public void addSongToRoom(Long roomCode, SongSummary song) {
        String key = SONGLIST_PREFIX + roomCode + ":songs";
        objectRedisTemplate.opsForList().rightPush(key, song); // 리스트 맨 뒤에 추가
    }

    // 노래 리스트 전체 조회
    public List<SongSummary> getSongsInRoom(Long roomCode) {
        String key = SONGLIST_PREFIX + roomCode + ":songs";
        List<Object> objectList = objectRedisTemplate.opsForList().range(key, 0, -1);

        List<SongSummary> result = new ArrayList<>();
        for (Object item : objectList) {
            if (item instanceof SongSummary) {
                result.add((SongSummary) item);
            }
        }
        return result;
    }
}
