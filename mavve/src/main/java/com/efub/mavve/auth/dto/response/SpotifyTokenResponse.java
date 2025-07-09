package com.efub.mavve.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SpotifyTokenResponse {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
    private String scope;
}
