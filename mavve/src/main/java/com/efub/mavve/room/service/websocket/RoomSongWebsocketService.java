package com.efub.mavve.room.service.websocket;

import com.efub.mavve.room.payload.request.AddSongRequestPayload;
import com.efub.mavve.room.payload.request.DeleteSongRequestPayload;
import com.efub.mavve.room.payload.response.AddSongResponsePayload;
import com.efub.mavve.room.payload.response.DeleteSongResponsePayload;
import com.efub.mavve.room.payload.response.NextSongResponsePayload;
import com.efub.mavve.room.payload.summary.SongSummary;
import com.efub.mavve.room.service.redis.RoomSongRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RoomSongWebsocketService {
    private final RoomSongRedisService songRedisService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ThreadPoolTaskScheduler taskScheduler;

    public AddSongResponsePayload addSong(Long roomCode, AddSongRequestPayload request) {
        //TODO: 노래검색 함수 생성 후 검색한 노래로 수정
        songRedisService.addSong(roomCode, request.getSong());
        return AddSongResponsePayload.from(request.getSong());
    }

    public DeleteSongResponsePayload deleteSongs(Long roomCode, DeleteSongRequestPayload request) {
        songRedisService.deleteSongs(roomCode, request.getSongIds());
        return DeleteSongResponsePayload.from(request.getSongIds());
    }

    // 다음 노래 예약 스케쥴링
    private void scheduleNextSong(Long roomCode, SongSummary currentSong){
        LocalDateTime nextSongStartTime = LocalDateTime.now().plusSeconds(currentSong.getDuration());
        SongSummary nextSong = songRedisService.getNextSong(roomCode, currentSong);

        //TODO: 다음노래 예약해놓았는데, 해당 노래 삭제한 경우 다음 노래 변환 로직 추가
        taskScheduler.schedule(() -> {
            broadcastNextSong(roomCode, nextSong, nextSongStartTime);
        }, Instant.from(nextSongStartTime));
    }

    // 다음 노래 전환 브로드캐스트
    private void broadcastNextSong(Long roomCode, SongSummary nextSong, LocalDateTime startTime){
        songRedisService.addCurrentSong(roomCode, nextSong, startTime);
        messagingTemplate.convertAndSend("/topic/room/" + roomCode, NextSongResponsePayload.from(nextSong));
    }
}
