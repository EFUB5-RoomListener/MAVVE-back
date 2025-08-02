package com.efub.mavve.playlist.repository;

import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.playlist.domain.PlaylistSong;
import com.efub.mavve.songs.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    Optional<PlaylistSong> findByPlaylistAndSong(Playlist playlist, Song song);
}
