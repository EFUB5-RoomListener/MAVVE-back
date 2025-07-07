package com.efub.mavve.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
public class KakaoTokenResponse {
    private final String token_type;
    private final String access_token;
    private final String refresh_token;
    private final String expires_in;
    private final String refresh_expires_in;
}
