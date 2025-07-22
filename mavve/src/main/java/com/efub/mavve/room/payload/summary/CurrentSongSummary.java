package com.efub.mavve.room.payload.summary;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrentSongSummary {
    SongRedis song;
    LocalDateTime startTime;

    public static CurrentSongSummary from(SongRedis song, LocalDateTime startTime){
        return CurrentSongSummary.builder()
                .song(song)
                .startTime(startTime)
                .build();
    }
}
