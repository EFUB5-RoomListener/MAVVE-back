package com.efub.mavve.room.controller.websocket;

import com.efub.mavve.room.payload.request.ChatRequestPayload;
import com.efub.mavve.room.payload.response.ChatResponsePayload;
import com.efub.mavve.room.service.RoomChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class RoomChatWebsocketController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final RoomChatService roomChatService;

    @MessageMapping("/rooms/{roomCode}/chats")
    public void chat(@DestinationVariable Long roomCode,
                     @Payload ChatRequestPayload request,
                     Principal principal) {
        ChatResponsePayload response = roomChatService.createAndSendMessage(roomCode, request, principal);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomCode + "/chats", response);
    }
}