package com.efub.mavve.room.dto.summary;

import com.efub.mavve.auth.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomUserSummary {
    private String profileImg;
    private String nickname;

    public static RoomUserSummary from(User user){
        return RoomUserSummary.builder()
                .profileImg(user.getUserImageUrl())
                .nickname(user.getUsername())
                .build();
    }
}
