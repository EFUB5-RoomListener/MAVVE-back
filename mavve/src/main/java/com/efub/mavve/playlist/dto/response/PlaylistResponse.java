package com.efub.mavve.playlist.dto.response;

import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.songs.dto.response.SongResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PlaylistResponse(Long playlistId,
                               String username,
                               String name,
                               String playImageUrl,
                               LocalDateTime createdAt,
                               List<SongResponse> songs) {

    public static PlaylistResponse from(Playlist playlist) {
        List<SongResponse> songResponses = playlist.getPlaylistSongs().stream()
                .map(playlistSong -> SongResponse.fromSongEntity(playlistSong.getSong()))
                .collect(Collectors.toList());

        return new PlaylistResponse(
                playlist.getPlaylistId(),
                playlist.getUser().getUsername(),
                playlist.getName(),
                playlist.getPlayImageUrl(),
                playlist.getCreatedAt(),
                songResponses
        );
    }
}
