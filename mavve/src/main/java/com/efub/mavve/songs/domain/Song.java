package com.efub.mavve.songs.domain;

import com.efub.mavve.artist.domain.Artist;
import com.efub.mavve.playlist.domain.PlaylistSong;
import com.efub.mavve.songs.dto.response.spotify.SongInfoResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long songId;

    @Column(nullable = false)
    String spotifySongId;

    @Column(nullable = false)
    String album;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    int duration;

    @Column(nullable = false)
    String coverImageUrl;

    @OneToMany(mappedBy = "song")
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SongArtist> songArtists = new ArrayList<>();

    public void addArtist(Artist artist) {
        SongArtist songArtist = SongArtist.create(this, artist);
        this.songArtists.add(songArtist);
        artist.getSongArtists().add(songArtist);
    }

    public void addArtists(List<Artist> artists) {
        for (Artist artist : artists) {
            this.addArtist(artist);
        }
    }

    @Builder
    public Song(String spotifySongId, String album, String title, int duration, String coverImageUrl) {
        this.album = album;
        this.title = title;
        this.duration = duration;
        this.coverImageUrl = coverImageUrl;
        this.spotifySongId = spotifySongId;
    }

    public static Song fromSongInfoResponse(SongInfoResponse songInfoResponse) {
        return Song.builder()
                .spotifySongId(songInfoResponse.getId())
                .album(songInfoResponse.getAlbum().getName())
                .title(songInfoResponse.getName())
                .duration(songInfoResponse.getDuration_ms())
                .coverImageUrl(songInfoResponse.getAlbum().getImages().get(0).getUrl())
                .build();
    }
}
