package com.efub.mavve.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SpotifyRedirctUri {
    private final String uri;

    public static SpotifyRedirctUri of(String uri) {
        return new SpotifyRedirctUri(uri);
    }
}
