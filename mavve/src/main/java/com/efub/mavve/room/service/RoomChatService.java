package com.efub.mavve.room.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.domain.RoomChat;
import com.efub.mavve.room.payload.request.ChatRequestPayload;
import com.efub.mavve.room.payload.response.ChatResponsePayload;
import com.efub.mavve.room.repository.RoomChatRepository;
import com.efub.mavve.room.service.websocket.PrincipalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomChatService {
    private final RoomChatRepository roomChatRepository;
    private final RoomService roomService;
    private final PrincipalUtil principalUtil;

    @Transactional
    public ChatResponsePayload createAndSendMessage(Long roomCode, ChatRequestPayload request, Principal principal) {
        User user = principalUtil.principalToUser(principal);
        Room room = roomService.findByRoomId(roomCode);

        RoomChat roomChat = request.toEntity(user, room);
        roomChatRepository.save(roomChat);
        return ChatResponsePayload.from(roomChat);
    }
}
