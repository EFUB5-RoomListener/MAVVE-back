package com.efub.mavve.room.dto.request;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.domain.Room;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoomUpdateRequest (String roomName,
                                List<String> tag,
                                boolean isPublic){
}
