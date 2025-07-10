package com.efub.mavve.room.service;

import com.efub.mavve.room.dto.request.AddSongRequest;
import com.efub.mavve.room.dto.response.AddSongResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoomWebsocketService {
    private final RoomRedisService roomRedisService;

    public AddSongResponse addSong(Long roomCode, AddSongRequest request) {
        roomRedisService.addSongToRoom(roomCode, request.getSong());
        return AddSongResponse.from(request.getSong());
    }
}
