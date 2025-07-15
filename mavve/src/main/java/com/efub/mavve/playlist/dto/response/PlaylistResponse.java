package com.efub.mavve.playlist.dto.response;

import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.songs.dto.response.SongResponse;

import java.util.List;
import java.util.stream.Collectors;

public record PlaylistResponse(Long playlistId,
                               Long userId,
                               String name,
                               String playImageUrl,
                               List<SongResponse> songs) {

    public static PlaylistResponse from(Playlist playlist) {
        List<SongResponse> songResponses = playlist.getPlaylistSongs().stream()
                .map(playlistSong -> SongResponse.from(playlistSong.getSong()))
                .collect(Collectors.toList());

        return new PlaylistResponse(
                playlist.getPlaylistId(),
                playlist.getUser().getUserId(),
                playlist.getName(),
                playlist.getPlayImageUrl(),
                songResponses
        );
    }
}
