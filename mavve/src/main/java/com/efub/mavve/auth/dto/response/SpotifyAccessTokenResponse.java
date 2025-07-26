package com.efub.mavve.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class SpotifyAccessTokenResponse {
    private final String spotifyAccessToken;
}
