package com.efub.mavve.room.controller;

import com.efub.mavve.room.dto.request.AddSongRequest;
import com.efub.mavve.room.dto.response.AddSongResponse;
import com.efub.mavve.room.service.RoomWebsocketService;
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
    private final RoomWebsocketService roomWebsocketService;

    @MessageMapping("/rooms/{roomCode}/add-song")
    public void addSong(@DestinationVariable Long roomCode, @Payload AddSongRequest request) {
        AddSongResponse response = roomWebsocketService.addSong(roomCode, request);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomCode, response);
    }
}
