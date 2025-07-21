package com.efub.mavve.room.dto.response;

import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.payload.summary.CurrentSongSummary;
import com.efub.mavve.room.payload.summary.SongSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomEnterResponse {
    String roomName;
    String createdBy;
    int songCount;
    String totalDuration;
    List<SongSummary> songs;
    CurrentSongSummary currentSong;

    public static RoomEnterResponse from(Room room, List<SongSummary> songList, String duration, CurrentSongSummary currentSong){
        return RoomEnterResponse.builder()
                .roomName(room.getRoomName())
                .createdBy(room.getUser().getUsername())
                .songCount(songList.size())
                .totalDuration(duration)
                .songs(songList)
                .currentSong(currentSong)
                .build();
    }
}
