package com.efub.mavve.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoCodeRequest {
    @NotBlank(message = "인가코드가 필요합니다.")
    private final String code;
}
