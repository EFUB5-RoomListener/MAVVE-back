package com.efub.mavve.room.service;

import com.efub.mavve.global.exception.ClientExceptionCode;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.room.dto.summary.SongSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomRedisService {

    private final RedisTemplate<String, Object> objectRedisTemplate;
    private static final String ROOM_PREFIX = "room:";
    private static final String SONGLIST_SUFFIX = ":songs";
    private static final String CURRENTSONG_SUFFIX = ":current";

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

    // 노래 리스트 삭제
    public void deleteSongsInRoom(Long roomCode, List<String> songIdsToRemove) {
        String key = ROOM_PREFIX + roomCode + SONGLIST_SUFFIX;
        List<SongSummary> songsInRoom = getSongsInRoom(roomCode);

        for(SongSummary songSummary: songsInRoom){
            if(songIdsToRemove.contains(songSummary.getSongId())){
                objectRedisTemplate.opsForList().remove(key, 1, songSummary);   //1개 삭제
            }
        }
    }

    // 다음 노래 찾기
    public SongSummary getNextSong(Long roomCode, SongSummary currentSong){
        List<SongSummary> songsInRoom = getSongsInRoom(roomCode);

        for (int i = 0; i < songsInRoom.size(); i++) {
            if (songsInRoom.get(i).equals(currentSong)) {
                return songsInRoom.get((i + 1) % songsInRoom.size());   //마지막 노래라면 첫번째 노래 다시 재생
            }
        }
        throw new MavveException(ExceptionCode.SONG_NOT_FOUND);
    }

    // 현재 재생되고 있는 노래 저장 (기존 것 삭제)
    public void addCurrentSong(Long roomCode, SongSummary song, LocalDateTime startTime){
        String key = ROOM_PREFIX + roomCode + CURRENTSONG_SUFFIX;
        objectRedisTemplate.delete(key);    // 이전 노래 map 삭제

        Map<String, Object> map = new HashMap<>();
        map.put("song", song);
        map.put("startTime", startTime);
        objectRedisTemplate.opsForHash().putAll(key, map);
    }

}
