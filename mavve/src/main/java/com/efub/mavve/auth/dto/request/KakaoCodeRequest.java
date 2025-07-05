package com.efub.mavve.auth.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoCodeRequest {
    private final String code;
}
