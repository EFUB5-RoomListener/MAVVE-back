package com.efub.mavve.songs.repository;

import com.efub.mavve.songs.domain.SongArtist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongArtistRepository extends JpaRepository<SongArtist, Long> {
    //ArtistId로 찾기
    List<SongArtist> findBySong_SongId(Long songId);
}
