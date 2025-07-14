package com.efub.mavve.auth.repository;

import com.efub.mavve.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsBySpotifyUserId(String spotifyUserId);
    Optional<User> findBySpotifyUserId(String spotifyUserId);
    Optional<User> findByUserId(Long userId);
}
