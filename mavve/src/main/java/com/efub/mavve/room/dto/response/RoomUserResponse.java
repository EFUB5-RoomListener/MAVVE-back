package com.efub.mavve.room.dto.response;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.dto.summary.RoomUserSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomUserResponse {
    private List<RoomUserSummary> users;

    public static RoomUserResponse from(List<RoomUserSummary> users){
        return RoomUserResponse.builder()
                .users(users)
                .build();
    }
}
