package com.efub.mavve.songs.service.spotify;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SpotifyProperties {
    private final String serchRequestUri;
    private final String getTrackUri;
    private final String songInPlaylistRequestUri;
    private final String playlistId;

    public SpotifyProperties(@Value("${spotify.search-uri}") String serchRequestUri,
                             @Value("${spotify.get-track-uri}") String getTrackUri,
                             @Value("${spotify.get-track-in-playlist-uri}") String songInPlaylistRequestUri,
                             @Value("${spotify.playlist-id}") String playlistId) {
        this.serchRequestUri = serchRequestUri;
        this.getTrackUri = getTrackUri;
        this.songInPlaylistRequestUri = songInPlaylistRequestUri;
        this.playlistId = playlistId;
    }

    public String getSerchSongRequestUri(String query, int limit, int offset) {
        return UriComponentsBuilder.fromUriString(serchRequestUri)
                .queryParam("type", SearchType.track.toString())
                .queryParam("q", query)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .build().toUriString();
    }

    public String getSongInfoRequestUri(String spotifySongId){
        return UriComponentsBuilder.fromUriString(getTrackUri)
                .path("/" + spotifySongId)
                .build().toUriString();
    }

    public String getSongInPlaylistRequestUri(int limit, int offset){
        return UriComponentsBuilder.fromUriString(songInPlaylistRequestUri)
                .path("/" + playlistId)
                .path("/tracks")
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .build().toUriString();
    }
}
