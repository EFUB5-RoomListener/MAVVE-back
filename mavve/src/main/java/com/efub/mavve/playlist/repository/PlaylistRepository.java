package com.efub.mavve.playlist.repository;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.playlist.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Playlist findByPlaylistId(Long playlistId);
    boolean existsByName(String name);
    List<Playlist> findByOrderByPlaylistIdDesc();
    List<Playlist> findByUserAndNameContainingIgnoreCaseOrderByCreatedAtDesc(User user, String keyword);
}