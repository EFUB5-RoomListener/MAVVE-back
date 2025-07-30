package com.efub.mavve.room.controller.websocket;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.payload.request.AddSongRequestPayload;
import com.efub.mavve.room.payload.request.DeleteSongRequestPayload;
import com.efub.mavve.room.payload.response.AddSongResponsePayload;
import com.efub.mavve.room.payload.response.DeleteSongResponsePayload;
import com.efub.mavve.room.payload.response.MessageType;
import com.efub.mavve.room.payload.response.SubscribeResponsePayload;
import com.efub.mavve.room.service.websocket.RoomSongWebsocketService;
import lombok.RequiredArgsConstructor; 
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RoomSongWebsocketController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final RoomSongWebsocketService roomSongWebsocketService;

    @SubscribeMapping("/rooms/{roomCode}/songs")
    public SubscribeResponsePayload subscribe(@DestinationVariable Long roomCode,
                                              Principal principal,
                                              Message<?> message) {
        User user = roomSongWebsocketService.subscribe(roomCode, principal, message);
        log.info("user{} subscribe {} room", user.getUserId(), roomCode);
        return new SubscribeResponsePayload(MessageType.SUBSCRIBE_COMPLETE);
    }

    @MessageMapping("/rooms/{roomCode}/add-song")
    public void addSong(@DestinationVariable Long roomCode, @Payload AddSongRequestPayload request) {
        AddSongResponsePayload response = roomSongWebsocketService.addSong(roomCode, request);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomCode + "/songs", response);
    }

    @MessageMapping("/rooms/{roomCode}/delete-song")
    public void deleteSongs(@DestinationVariable Long roomCode, @Payload DeleteSongRequestPayload request){
        DeleteSongResponsePayload response = roomSongWebsocketService.deleteSongs(roomCode, request);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomCode + "/songs", response);
    }
}
