package com.efub.mavve.room.repository;

import com.efub.mavve.room.domain.RoomChat;
import com.efub.mavve.room.dto.summary.ChatSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomChatRepository extends JpaRepository<RoomChat, Long> {
    @Query("""
            SELECT new com.efub.mavve.room.dto.summary.ChatSummary(c.roomChatId, c.content, c.createdAt, u.username, u.userImageUrl) 
            FROM RoomChat c 
            JOIN c.user u 
            WHERE c.room.roomId = :roomId 
            AND (:lastChatId is NULL OR c.roomChatId < :lastChatId) 
            ORDER BY c.createdAt DESC
            """)
    List<ChatSummary> findChatsByRoomId(@Param("roomId") Long roomId, @Param("lastChatId") Long lastChatId, Pageable pageable);
}
