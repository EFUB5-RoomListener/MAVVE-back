package com.efub.mavve.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class SpotifyUserInfoResponse {
    private final String id;
    private final String display_name;
    private final String email;
    private final String country;
    private final SpotifyTokenResponse token;

    public static SpotifyUserInfoResponse fromSpotifyTokenResponse(String id,
                                                                   String display_name,
                                                                   String email,
                                                                   String country,
                                                                   final SpotifyTokenResponse token) {
        return SpotifyUserInfoResponse.builder()
                .id(id)
                .display_name(display_name)
                .email(email)
                .country(country)
                .token(token)
                .build();
    }
}
