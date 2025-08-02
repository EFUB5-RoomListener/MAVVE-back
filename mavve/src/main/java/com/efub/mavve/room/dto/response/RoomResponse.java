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

    private Long roomId;
    private Long userId;
    private String roomName;
    private String userName;
    private Long viewCount;
    private int likeCount;
    private List<String> tag;
    private String imageURL;
    private boolean isPublic;
    private LocalDateTime createdAt;
    private boolean liked;
    private String duration;


    public static RoomResponse from(Room room, int likeCount, boolean liked, String duration){
        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .userId(room.getUser().getUserId())
                .roomName(room.getRoomName())
                .userName(room.getUser().getUsername())
                .viewCount(room.getViewCount())
                .likeCount(likeCount)
                .liked(liked)
                .tag(room.getTag())
                .imageURL(room.getImageURL())
                .isPublic(room.isPublic())
                .createdAt(room.getCreatedAt())
                .duration(duration)
                .build();
    }

}
