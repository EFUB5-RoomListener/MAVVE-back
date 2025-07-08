package com.efub.mavve.playlist.dto.response;

import com.efub.mavve.playlist.domain.Playlist;

public record PlaylistResponse(Long playlistId,
                               Long userId,
                               String name,
                               String playImageUrl) {

    public static PlaylistResponse from(Playlist playlist) {
        return new PlaylistResponse(
                playlist.getPlaylistId(),
                playlist.getUser().getUserId(),
                playlist.getName(),
                playlist.getPlayImageUrl()
        );
    }
}
