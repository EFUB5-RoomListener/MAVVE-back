package com.efub.mavve.room.service.redis;

import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.room.payload.summary.SongSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomSongRedisService {
    private final RedisTemplate<String, Object> objectRedisTemplate;

    // 노래 추가
    public void addSong(Long roomCode, SongSummary song) {
        try{
            String key = RoomRedisKeyUtils.getSongListKey(roomCode);
            objectRedisTemplate.opsForList().rightPush(key, song); // 리스트 맨 뒤에 추가
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }

    // 노래 리스트 추가
    public void addSongs(Long roomCode, List<SongSummary> songList){
        try{
            String key = RoomRedisKeyUtils.getSongListKey(roomCode);
            objectRedisTemplate.opsForList().rightPush(key, songList.toArray()); // 리스트 맨 뒤에 추가
        } catch (Exception e) {
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }

    // 노래 리스트 전체 조회
    public List<SongSummary> getAllSongs(Long roomCode) {
        String key = RoomRedisKeyUtils.getSongListKey(roomCode);
        List<Object> objectList = objectRedisTemplate.opsForList().range(key, 0, -1);

        if(objectList == null) return Collections.emptyList();

        List<SongSummary> result = new ArrayList<>();
        for (Object item : objectList) {
            if (item instanceof SongSummary) {
                result.add((SongSummary) item);
            } else {
                throw new MavveException(ExceptionCode.REDIS_DESERIALIZATION_ERROR);
            }
        }
        return result;
    }

    // 노래 리스트 삭제
    public void deleteSongs(Long roomCode, List<String> songIdsToRemove) {
        try{
            String key = RoomRedisKeyUtils.getSongListKey(roomCode);
            List<SongSummary> songsInRoom = getAllSongs(roomCode);

            for(SongSummary songSummary: songsInRoom){
                if(songIdsToRemove.contains(songSummary.getSongId())){
                    objectRedisTemplate.opsForList().remove(key, 1, songSummary);   //1개 삭제
                }
            }
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_DELETE_ERROR);
        }
    }

    // 다음 노래 찾기
    public SongSummary getNextSong(Long roomCode, SongSummary currentSong){
        List<SongSummary> songsInRoom = getAllSongs(roomCode);

        // NOTE: 에러처리가 아니라 song에 null값 담아서 보낼지 추가 고민
        if (songsInRoom.isEmpty()) {
            throw new MavveException(ExceptionCode.SONG_LIST_EMPTY);
        }

        for (int i = 0; i < songsInRoom.size(); i++) {
            if (songsInRoom.get(i).equals(currentSong)) {
                return songsInRoom.get((i + 1) % songsInRoom.size());   //마지막 노래라면 첫번째 노래 다시 재생
            }
        }
        throw new MavveException(ExceptionCode.SONG_NOT_FOUND);
    }

    // 현재 재생되고 있는 노래 저장 (기존 것 삭제)
    public void addCurrentSong(Long roomCode, SongSummary song, LocalDateTime startTime){
        try{
            String key = RoomRedisKeyUtils.getCurrentSongKey(roomCode);
            objectRedisTemplate.delete(key);    // 이전 노래 map 삭제

            Map<String, Object> map = new HashMap<>();
            map.put("song", song);
            map.put("startTime", startTime);
            objectRedisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }
}
