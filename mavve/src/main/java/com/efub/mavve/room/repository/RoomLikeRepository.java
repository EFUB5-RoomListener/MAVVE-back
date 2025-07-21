package com.efub.mavve.room.repository;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.domain.RoomLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomLikeRepository extends JpaRepository<RoomLike, Long> {

    Optional<RoomLike> findByUserAndRoom(User user, Room room);
    boolean existsByUserAndRoom(User user, Room room);
    int countByRoom(Room room);
}
