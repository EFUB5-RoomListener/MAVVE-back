package com.efub.mavve.room.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.service.jwt.CustomUserDetails;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.domain.RoomChat;
import com.efub.mavve.room.payload.request.ChatRequestPayload;
import com.efub.mavve.room.payload.response.ChatResponsePayload;
import com.efub.mavve.room.repository.RoomChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomChatService {
    private final RoomChatRepository roomChatRepository;
    private final RoomService roomService;

    @Transactional
    public ChatResponsePayload createAndSendMessage(Long roomCode, ChatRequestPayload request, Principal principal) {
        User user = principalToUser(principal);
        Room room = roomService.findByRoomId(roomCode);

        RoomChat roomChat = request.toEntity(user, room);
        roomChatRepository.save(roomChat);
        return ChatResponsePayload.from(roomChat);
    }

    private User principalToUser(Principal principal){
        User user = (User) ((Authentication) principal).getPrincipal();
        return user;
    }
}
