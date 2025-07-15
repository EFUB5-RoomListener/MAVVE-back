package com.efub.mavve.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class RoomListResponse{
    private Integer totalCount;
    private List<RoomResponse> roomList;

    public static RoomListResponse from(List<RoomResponse> roomList){
        return RoomListResponse.builder()
                .totalCount(roomList.size())
                .roomList(roomList)
                .build();
    }
}
