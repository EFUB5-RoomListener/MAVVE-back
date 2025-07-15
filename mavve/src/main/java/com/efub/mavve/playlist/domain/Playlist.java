package com.efub.mavve.playlist.domain;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.songs.domain.Song;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

    @Builder
    public Playlist(User user, String name, String playImageUrl) {
        this.user = user;
        this.name = name;
        this.playImageUrl = playImageUrl;
    }

    public void changePlaylist(String newName, String newPlayImageUrl){
        this.name = newName;
        this.playImageUrl = newPlayImageUrl;
    }

    public void addSong(Song song){
        PlaylistSong playlistSong = PlaylistSong.create(this, song);
        this.playlistSongs.add(playlistSong);
        song.getPlaylistSongs().add(playlistSong);
    }

}
