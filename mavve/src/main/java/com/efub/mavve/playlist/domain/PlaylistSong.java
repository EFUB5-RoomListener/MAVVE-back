package com.efub.mavve.playlist.domain;

import com.efub.mavve.songs.domain.Song;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "playlist-song")
public class PlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistSongId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    private Song song;
}
