package com.efub.mavve.room.dto.summary;

import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.dto.response.RoomHotResponse;
import com.efub.mavve.room.payload.summary.SongRedis;
import com.efub.mavve.room.payload.summary.SongSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomHotSummary {
    private Long roomId;
    private String roomName;
    private List<String> tag;
    private String imageURL;
    private SongRedis song;

    public static RoomHotSummary from(Room room, SongRedis song){
        return RoomHotSummary.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .tag(room.getTag())
                .song(song)
                .build();
    }
}
