package com.efub.mavve.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoRequest {
    @NotBlank(message = "닉네임을 입력해야합니다.")
    private final String nickname;
    @NotNull(message = "프로필 이미지를 입력해야합니다.")
    private final String profile;
}
