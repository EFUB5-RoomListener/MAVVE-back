package com.efub.mavve.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoomLikeResponse {
    private Long roomId;
    private boolean liked;
    private int likeCount;
}
