package com.efub.mavve.songs.service.spotify;

import com.efub.mavve.songs.dto.response.spotify.SearchSongResponse;
import com.efub.mavve.songs.dto.response.spotify.SongInfoResponse;
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
    private final String headerName = "Authorization";
    private final String headerValue = "Bearer ";

    public SearchSongResponse getSongSearchResult(String query, String accessToken, int limit, int offset) {
        String searchRequestUri = spotifyProperties.getSerchSongRequestUri(query, limit, offset);
        return restClient.get()
                .uri(searchRequestUri)
                .header(headerName, headerValue + accessToken)
                .retrieve()
                .body(SearchSongResponse.class);
    }

    public SongInfoResponse getSongInfo(String spotifySongId, String accessToken){
        String songInfoRequestUri = spotifyProperties.getSongInfoRequestUri(spotifySongId);
        return restClient.get()
                .uri(songInfoRequestUri)
                .header(headerName, headerValue + accessToken)
                .retrieve()
                .body(SongInfoResponse.class);
    }


}
