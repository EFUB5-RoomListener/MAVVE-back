package com.efub.mavve.songs.domain;

import com.efub.mavve.artist.domain.Artist;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class SongArtist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songArtistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Builder
    public SongArtist(Song song, Artist artist) {
        this.song = song;
        this.artist = artist;
    }

    public static SongArtist create(Song song, Artist artist) {
        return SongArtist.builder().song(song).artist(artist).build();
    }
}
