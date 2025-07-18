package com.efub.mavve.playlist.domain;

import com.efub.mavve.songs.domain.Song;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaylistSong {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistSongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

    public static PlaylistSong create(Playlist playlist, Song song){
        return PlaylistSong.builder()
                .playlist(playlist)
                .song(song)
                .build();
    }

}
