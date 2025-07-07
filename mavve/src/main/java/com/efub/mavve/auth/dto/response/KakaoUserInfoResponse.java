package com.efub.mavve.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class KakaoUserInfoResponse {
    private final Long id;
    private KakaoAccountResponse kakao_account;
}
