package com.efub.mavve.artist.repository;

import com.efub.mavve.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByArtistSpotifyId(String artistSpotifyId);
}
