package com.efub.mavve.room.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatSummary {
    Long chatId;
    String content;
    LocalDateTime createdAt;
    String nickname;
    String profileImg;
}
