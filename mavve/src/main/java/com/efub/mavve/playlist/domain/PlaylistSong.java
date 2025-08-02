package com.efub.mavve.playlist.domain;

import com.efub.mavve.songs.domain.Song;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PlaylistSong {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistSongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

    @CreatedDate
    private LocalDateTime createdAt;

    public static PlaylistSong create(Playlist playlist, Song song){
        return PlaylistSong.builder()
                .playlist(playlist)
                .song(song)
                .build();
    }

}
