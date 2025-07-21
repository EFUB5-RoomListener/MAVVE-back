package com.efub.mavve.room.dto.response;

import com.efub.mavve.room.dto.summary.ChatSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatListResponse {
    List<ChatSummary> chats;
}
