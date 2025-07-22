package com.efub.mavve.room.repository;

import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.domain.RoomPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomPlaylistRepository extends JpaRepository<RoomPlaylist, Long> {
    //@Query("SELECT rp.playlist FROM RoomPlaylist rp WHERE rp.room = :room")
    List<RoomPlaylist> findByRoom(Room room);
}
