package com.efub.mavve.artist.domain;

import com.efub.mavve.songs.domain.SongArtist;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long artistId;

    private String artistName;

    private String artistSpotifyId;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SongArtist> songArtists = new ArrayList<>();

    public Artist(String artistName, String artistSpotifyId) {
        this.artistName = artistName;
        this.artistSpotifyId = artistSpotifyId;
    }

    public static Artist create(String artistName, String artistSpotifyId) {
        return new Artist(artistName, artistSpotifyId);
    }
}

