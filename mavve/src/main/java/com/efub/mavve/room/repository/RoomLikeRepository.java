package com.efub.mavve.room.repository;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.domain.RoomLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomLikeRepository extends JpaRepository<RoomLike, Long> {

    Optional<RoomLike> findByUserAndRoom(User user, Room room);
    boolean existsByUserAndRoom(User user, Room room);
    int countByRoom(Room room);

    // 사용자의 좋아요를 누른 방 찾기
    @Query("SELECT rl.room FROM RoomLike rl WHERE rl.user = :user")
    List<Room> findRoomByUser(@Param("user") User user);
}
