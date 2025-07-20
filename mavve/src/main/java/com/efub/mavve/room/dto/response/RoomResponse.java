package com.efub.mavve.room.dto.response;

import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.repository.RoomLikeRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class RoomResponse {
    private final RoomLikeRepository roomLikeRepository;

    private Long roomId;
    private Long userId;
    private String roomName;
    private Long viewCount;
    private int likeCount;
    private List<String> tag;
    private boolean isPublic;
    private LocalDateTime createdAt;


    public static RoomResponse from(Room room, int likeCount){
        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .userId(room.getUser().getUserId())
                .roomName(room.getRoomName())
                .viewCount(room.getViewCount())
                .likeCount(likeCount)
                .tag(room.getTag())
                .isPublic(room.isPublic())
                .createdAt(room.getCreatedAt())
                .build();
    }

}
