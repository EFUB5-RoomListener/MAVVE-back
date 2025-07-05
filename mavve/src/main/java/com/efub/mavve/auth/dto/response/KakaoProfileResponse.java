package com.efub.mavve.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoProfileResponse {
    private final String nickname;
}
