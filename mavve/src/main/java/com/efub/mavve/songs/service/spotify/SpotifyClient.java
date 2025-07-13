package com.efub.mavve.songs.service.spotify;

import com.efub.mavve.songs.dto.response.SearchSongResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyClient {
    private final RestClient restClient;
    private final SpotifyProperties spotifyProperties;

    public SearchSongResponse getSongSearchResult(String query, String accessToken, int limit, int offset) {
        String headerName = "Authorization";
        String headerValue = "Bearer " +  accessToken;

        String searchRequestUri = spotifyProperties.getSerchSongRequestUri(query, limit, offset);
        return restClient.get()
                .uri(searchRequestUri)
                .header(headerName, headerValue)
                .retrieve()
                .body(SearchSongResponse.class);
    }
}
