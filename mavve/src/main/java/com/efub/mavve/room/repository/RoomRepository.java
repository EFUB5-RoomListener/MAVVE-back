package com.efub.mavve.room.repository;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.dto.projection.RoomLikeCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // Room Id로 조회하기
    Optional<Room> findByRoomId(long roomId);

    // 최신순 조회
    List<Room> findAllByOrderByRoomIdDesc();

    // 사용자의 방 리스트
    List<Room> findByUser(User user);

    //조회순
    List<Room> findTop5ByIsPublicTrueOrderByViewCountDesc();

    // 좋아요순
    @Query("SELECT r.roomId AS roomId, COUNT(rl) AS likeCount " +
            "FROM Room r LEFT JOIN RoomLike rl ON rl.room = r " +
            "WHERE r.isPublic = true " +
            "GROUP BY r " +
            "ORDER BY COUNT(rl) DESC")
    List<RoomLikeCountProjection> findTop5ByIsPublicTrueOrderByLikeCountDesc();

    // 검색
    List<Room> findByRoomNameContainingIgnoreCaseAndIsPublicTrueOrderByCreatedAtDesc(String keyword);



}
