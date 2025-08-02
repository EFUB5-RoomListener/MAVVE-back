package com.efub.mavve.room.service.redis;

import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.room.payload.summary.CurrentSongSummary;
import com.efub.mavve.room.payload.summary.SongRedis;
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
    public SongRedis addSong(Long roomCode, SongSummary request) {
        try{
            String key = RoomRedisKeyUtils.getSongListKey(roomCode);
            SongRedis song = SongSummary.toRedisPOJO(request);
            objectRedisTemplate.opsForList().rightPush(key, song); // 리스트 맨 뒤에 추가

            return song;
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }

    // 노래 리스트 추가
    public void addSongs(Long roomCode, List<SongRedis> songList){
        try {
            String key = RoomRedisKeyUtils.getSongListKey(roomCode);
            for (SongRedis song : songList) {
                objectRedisTemplate.opsForList().rightPush(key, song);
            }
        } catch (Exception e) {
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }

    // 처음 노래 조회
    public SongRedis getFirstSongInRoom(Long roomCode){
        String key = RoomRedisKeyUtils.getSongListKey(roomCode);
        return (SongRedis) objectRedisTemplate.opsForList().getFirst(key);
    }

    // 노래 리스트 전체 조회
    public List<SongRedis> getAllSongsInRoom(Long roomCode) {
        String key = RoomRedisKeyUtils.getSongListKey(roomCode);
        List<Object> objectList = objectRedisTemplate.opsForList().range(key, 0, -1);

        if(objectList == null) return Collections.emptyList();

        List<SongRedis> result = new ArrayList<>();
        for (Object item : objectList) {
            if (item instanceof SongRedis) {
                result.add((SongRedis) item);
            } else {
                throw new MavveException(ExceptionCode.REDIS_DESERIALIZATION_ERROR);
            }
        }
        return result;
    }

    // 현재 노래 리스트 있는지 확인
    public boolean hasSongs(Long roomCode){
        String key = RoomRedisKeyUtils.getSongListKey(roomCode);
        Long songCount = objectRedisTemplate.opsForList().size(key);

        return !(songCount == 0 || songCount == null);
    }

    // 노래 리스트 삭제
    public void deleteSongs(Long roomCode, List<String> songIdsToRemove) {
        try{
            String key = RoomRedisKeyUtils.getSongListKey(roomCode);
            List<SongRedis> songsInRoom = getAllSongsInRoom(roomCode);

            for(SongRedis song : songsInRoom){
                if(songIdsToRemove.contains(song.getSongId())){
                    objectRedisTemplate.opsForList().remove(key, 1, song);   //1개 삭제
                }
            }
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_DELETE_ERROR);
        }
    }

    // 다음 노래 찾기
    public SongRedis getNextSong(Long roomCode, SongRedis currentSong){
        List<SongRedis> songsInRoom = getAllSongsInRoom(roomCode);

        // NOTE: 에러처리가 아니라 song에 null값 담아서 보낼지 추가 고민
        if (songsInRoom.isEmpty()) {
            throw new MavveException(ExceptionCode.SONG_LIST_EMPTY);
        }

        for (int i = 0; i < songsInRoom.size(); i++) {
            if (songsInRoom.get(i).getSongId().equals(currentSong.getSongId())) {
                return songsInRoom.get((i + 1) % songsInRoom.size());   //마지막 노래라면 첫번째 노래 다시 재생
            }
        }
        throw new MavveException(ExceptionCode.SONG_NOT_FOUND);
    }

    // 현재 재생되고 있는 노래 저장 (기존 것 삭제)
    public void addCurrentSong(Long roomCode, SongRedis song, LocalDateTime startTime){
        try{
            String key = RoomRedisKeyUtils.getCurrentSongKey(roomCode);
            objectRedisTemplate.delete(key);    // 이전 노래 map 삭제

            Map<String, Object> map = new HashMap<>();
            map.put("song", song);
            map.put("startTime", startTime);
            objectRedisTemplate.opsForHash().putAll(key, map);
            log.info("add current song! : {}", song.getSongId());
        } catch (Exception e) {
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }

    // 현재 재생되고 있는 노래 조회
    public CurrentSongSummary getCurrentSong(Long roomCode){
        String key = RoomRedisKeyUtils.getCurrentSongKey(roomCode);
        SongRedis song = (SongRedis) objectRedisTemplate.opsForHash().get(key, "song");
        LocalDateTime startTime = (LocalDateTime) objectRedisTemplate.opsForHash().get(key, "startTime");

        return CurrentSongSummary.from(song, startTime);
    }

    // 현재 재생되고 있는 노래 있는지 확인
    public boolean hasCurrentSong(Long roomCode) {
        String key = RoomRedisKeyUtils.getCurrentSongKey(roomCode);
        return objectRedisTemplate.opsForHash().hasKey(key, "song");
    }

    // 현재 재생되고 있는 노래 삭제
    public void deleteCurrentSong(Long roomCode){
        try{
            String key = RoomRedisKeyUtils.getCurrentSongKey(roomCode);
            objectRedisTemplate.delete(key);
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }

    // 노래 id용 UUID값 생성
    public static String createUUID(){
        return UUID.randomUUID().toString();
    }

}
