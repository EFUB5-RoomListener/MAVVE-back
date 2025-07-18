package com.efub.mavve.auth.dto.response;

import com.efub.mavve.auth.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserInfoResponse {
    private final String nickname;
    private final String profile;

    public static UserInfoResponse fromUserEntity(User user){
        return UserInfoResponse.builder()
                .nickname(user.getUsername())
                .profile(user.getUserImageUrl())
                .build();
    }
}
