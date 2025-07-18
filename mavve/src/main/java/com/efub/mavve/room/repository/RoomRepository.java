package com.efub.mavve.room.repository;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    //Room Id로 조회하기
    Optional<Room> findByRoomId(long roomId);

    //사용자의 방 리스트
    List<Room> findByUser(User user);

    //조회순
    List<Room> findTop5ByIsPublicTrueOrderByViewCountDesc();

}
