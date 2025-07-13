package com.efub.mavve.room.service;

import com.efub.mavve.room.dto.request.AddSongRequest;
import com.efub.mavve.room.dto.request.DeleteSongRequest;
import com.efub.mavve.room.dto.response.AddSongResponse;
import com.efub.mavve.room.dto.response.DeleteSongResponse;
import com.efub.mavve.room.dto.response.NextSongResponse;
import com.efub.mavve.room.dto.summary.SongSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RoomWebsocketService {
    private final RoomRedisService roomRedisService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ThreadPoolTaskScheduler taskScheduler;

    public AddSongResponse addSong(Long roomCode, AddSongRequest request) {
        //TODO: 노래검색 함수 생성 후 검색한 노래로 수정
        roomRedisService.addSongToRoom(roomCode, request.getSong());
        return AddSongResponse.from(request.getSong());
    }

    public DeleteSongResponse deleteSongs(Long roomCode, DeleteSongRequest request) {
        roomRedisService.deleteSongsInRoom(roomCode, request.getSongIds());
        return DeleteSongResponse.from(request.getSongIds());
    }

    // 다음 노래 예약 스케쥴링
    private void scheduleNextSong(Long roomCode, SongSummary currentSong){
        LocalDateTime nextSongStartTime = LocalDateTime.now().plusSeconds(currentSong.getDuration());
        SongSummary nextSong = roomRedisService.getNextSong(roomCode, currentSong);

        //TODO: 다음노래 예약해놓았는데, 해당 노래 삭제한 경우 다음 노래 변환 로직 추가
        taskScheduler.schedule(() -> {
            broadcastNextSong(roomCode, nextSong, nextSongStartTime);
        }, Instant.from(nextSongStartTime));
    }

    // 다음 노래 전환 브로드캐스트
    private void broadcastNextSong(Long roomCode, SongSummary nextSong, LocalDateTime startTime){
        roomRedisService.addCurrentSong(roomCode, nextSong, startTime);
        messagingTemplate.convertAndSend("/topic/room/" + roomCode, NextSongResponse.from(nextSong));
    }
}
