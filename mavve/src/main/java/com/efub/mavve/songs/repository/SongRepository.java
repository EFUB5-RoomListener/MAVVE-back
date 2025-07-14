package com.efub.mavve.songs.repository;

import com.efub.mavve.songs.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {

    boolean existsBySpotifySongId(String spotifySongId);
    Optional<Song> findBySpotifySongId(String spotifySongId);

}
