package com.efub.mavve.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SpotifyUserInfoRaw {
    private final String id;
    private final String display_name;
    private final String email;
    private final String country;
}
