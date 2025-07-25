package com.efub.mavve.room.dto.request;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.domain.Room;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoomCreateRequest(@NotNull String roomName,
                                List<String> tag,
                                String imageURL,
                                @NotNull boolean isPublic
                                ) {
    public Room toEntity(User user){
        return Room.builder()
                .user(user)
                .roomName(roomName())
                .tag(tag())
                .imageURL(imageURL())
                .isPublic(isPublic())
                .build();
    }
}
