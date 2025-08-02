package com.efub.mavve.playlist.dto.summary;

import com.efub.mavve.playlist.domain.Playlist;

public record PlaylistSummary (Long playlistId,
                               String name,
                               String playImageUrl,
                               int songCount,
                               String totalDuration) {

    public static PlaylistSummary from(Playlist playlist) {
        return new PlaylistSummary(
                playlist.getPlaylistId(),
                playlist.getName(),
                playlist.getPlayImageUrl(),
                playlist.getSongCount(),
                playlist.getFormattedTotalDuration()
        );
    }
}