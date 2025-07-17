package com.efub.mavve.room.payload.response;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.domain.RoomChat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatResponsePayload {
    String nickname;
    String profileImg;
    String content;
    LocalDateTime createdAt;

    public static ChatResponsePayload from(RoomChat roomChat) {
        User user = roomChat.getUser();
        return ChatResponsePayload.builder()
                .profileImg(user.getUserImageUrl())
                .nickname(user.getUsername())
                .content(roomChat.getContent())
                .createdAt(roomChat.getCreatedAt())
                .build();
    }

}
