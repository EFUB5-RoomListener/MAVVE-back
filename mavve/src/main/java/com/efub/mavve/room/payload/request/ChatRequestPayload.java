package com.efub.mavve.room.payload.request;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.domain.RoomChat;
import lombok.Getter;

@Getter
public class ChatRequestPayload {
    String content;

    public RoomChat toEntity(User user, Room room) {
        return RoomChat.builder()
                .room(room)
                .user(user)
                .content(content)
                .build();
    }
}
