package com.efub.mavve.playlist.dto.response;

import com.efub.mavve.playlist.dto.summary.PlaylistSummary;

import java.util.List;

public record PlaylistListResponse (
        List<PlaylistSummary> playlists
) {
}
