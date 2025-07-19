package com.efub.mavve.room.service.websocket;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.payload.request.AddSongRequestPayload;
import com.efub.mavve.room.payload.request.DeleteSongRequestPayload;
import com.efub.mavve.room.payload.response.AddSongResponsePayload;
import com.efub.mavve.room.payload.response.DeleteSongResponsePayload;
import com.efub.mavve.room.payload.response.NextSongResponsePayload;
import com.efub.mavve.room.payload.summary.SongSummary;
import com.efub.mavve.room.service.redis.RoomSongRedisService;
import com.efub.mavve.room.service.redis.RoomUserRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomSongWebsocketService {
    private final RoomSongRedisService songRedisService;
    private final RoomUserRedisService userRedisService;
    private final PrincipalUtil principalUtil;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ThreadPoolTaskScheduler taskScheduler;

    private static final int BROADCAST_DELAY_SECONDS = 1;

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
    public void scheduleNextSong(Long roomCode, SongSummary currentSong){
        LocalDateTime nextSongStartTime = LocalDateTime.now().plusSeconds(currentSong.getDuration());
        SongSummary nextSong = songRedisService.getNextSong(roomCode, currentSong);
        Instant scheduleTime = nextSongStartTime
                .plusSeconds(BROADCAST_DELAY_SECONDS) // 전송 시간 고려해 1초 더 지연
                .atZone(ZoneId.systemDefault())
                .toInstant();

        //TODO: 다음노래 예약해놓았는데, 해당 노래 삭제한 경우 다음 노래 변환 로직 추가
        //TODO: 사용자가 있는 지 확인하고 스케쥴링
        taskScheduler.schedule(() -> {
            broadcastNextSong(roomCode, nextSong, nextSongStartTime);
        }, scheduleTime);
        log.info("next song schedule!");
    }

    // 다음 노래 전환 브로드캐스트
    private void broadcastNextSong(Long roomCode, SongSummary nextSong, LocalDateTime startTime){
        songRedisService.addCurrentSong(roomCode, nextSong, startTime);
        log.info("next song!");
        messagingTemplate.convertAndSend("/topic/rooms/" + roomCode + "/songs", NextSongResponsePayload.from(nextSong));
    }

    // 노래 정보 받을 주소 구독 -> 사용자 정보 저장
    public User subscribe(Long roomCode, Principal principal) {
        User user = principalUtil.principalToUser(principal);
        userRedisService.addUser(roomCode, user);
        return user;
    }
}
