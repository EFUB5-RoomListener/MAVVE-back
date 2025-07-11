package com.efub.mavve.room.service;

import com.efub.mavve.room.dto.request.AddSongRequest;
import com.efub.mavve.room.dto.request.DeleteSongRequest;
import com.efub.mavve.room.dto.response.AddSongResponse;
import com.efub.mavve.room.dto.response.DeleteSongResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoomWebsocketService {
    private final RoomRedisService roomRedisService;

    public AddSongResponse addSong(Long roomCode, AddSongRequest request) {
        //TODO: 노래검색 함수 생성 후 검색한 노래로 수정
        roomRedisService.addSongToRoom(roomCode, request.getSong());
        return AddSongResponse.from(request.getSong());
    }

    public DeleteSongResponse deleteSongs(Long roomCode, DeleteSongRequest request) {
        roomRedisService.deleteSongsInRoom(roomCode, request.getSongIds());
        return DeleteSongResponse.from(request.getSongIds());
    }
}
