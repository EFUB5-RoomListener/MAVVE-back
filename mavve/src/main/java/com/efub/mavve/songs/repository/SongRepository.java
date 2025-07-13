package com.efub.mavve.songs.repository;

import com.efub.mavve.songs.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {

}
