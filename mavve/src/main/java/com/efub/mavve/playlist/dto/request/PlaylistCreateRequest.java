package com.efub.mavve.playlist.dto.request;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.playlist.domain.Playlist;
import jakarta.validation.constraints.NotNull;

public record PlaylistCreateRequest(@NotNull String name,
                                    String playImageUrl) {

    public Playlist toEntity(User user) {
        return Playlist.builder()
                .user(user)
                .name(name)
                .playImageUrl(playImageUrl)
                .build();
    }
}
