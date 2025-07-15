package com.efub.mavve.room.controller.websocket;

import com.efub.mavve.room.payload.request.AddSongRequestPayload;
import com.efub.mavve.room.payload.request.DeleteSongRequestPayload;
import com.efub.mavve.room.payload.response.AddSongResponsePayload;
import com.efub.mavve.room.payload.response.DeleteSongResponsePayload;
import com.efub.mavve.room.service.websocket.RoomSongWebsocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomWebsocketController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final RoomSongWebsocketService roomSongWebsocketService;

    @MessageMapping("/rooms/{roomCode}/add-song")
    public void addSong(@DestinationVariable Long roomCode, @Payload AddSongRequestPayload request) {
        AddSongResponsePayload response = roomSongWebsocketService.addSong(roomCode, request);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomCode, response);
    }

    @MessageMapping("/rooms/{roomCode}/delete-song")
    public void deleteSongs(@DestinationVariable Long roomCode, @Payload DeleteSongRequestPayload request){
        DeleteSongResponsePayload response = roomSongWebsocketService.deleteSongs(roomCode, request);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomCode, response);
    }
}
