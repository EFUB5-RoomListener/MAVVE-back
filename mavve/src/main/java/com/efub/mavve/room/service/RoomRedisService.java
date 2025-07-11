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
    private static final String ROOM_PREFIX = "room:";
    private static final String SONGLIST_SUFFIX = ":songs";

    // 노래 추가
    public void addSongToRoom(Long roomCode, SongSummary song) {
        String key = ROOM_PREFIX + roomCode + SONGLIST_SUFFIX;
        objectRedisTemplate.opsForList().rightPush(key, song); // 리스트 맨 뒤에 추가
    }

    // 노래 리스트 전체 조회
    public List<SongSummary> getSongsInRoom(Long roomCode) {
        String key = ROOM_PREFIX + roomCode + SONGLIST_SUFFIX;
        List<Object> objectList = objectRedisTemplate.opsForList().range(key, 0, -1);

        List<SongSummary> result = new ArrayList<>();
        for (Object item : objectList) {
            if (item instanceof SongSummary) {
                result.add((SongSummary) item);
            }
        }
        return result;
    }

    //노래 리스트 삭제
    public void deleteSongsInRoom(Long roomCode, List<String> songIdsToRemove) {
        String key = ROOM_PREFIX + roomCode + SONGLIST_SUFFIX;
        List<SongSummary> songsInRoom = getSongsInRoom(roomCode);

        for(SongSummary songSummary: songsInRoom){
            if(songIdsToRemove.contains(songSummary.getSongId())){
                objectRedisTemplate.opsForList().remove(key, 1, songSummary);   //1개 삭제
            }
        }
    }
}
