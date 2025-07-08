package com.efub.mavve.playlist.domain;

import com.efub.mavve.auth.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "playlists")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String name;

    private String playImageUrl;

    @Builder
    public Playlist(User user, String name, String playImageUrl) {
        this.user = user;
        this.name = name;
        this.playImageUrl = playImageUrl;
    }

}
