package com.efub.mavve.room.payload.response;

import com.efub.mavve.room.payload.summary.SongRedis;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextSongResponsePayload {
    MessageType type;
    LocalDateTime startTime;
    SongRedis song;

    public static NextSongResponsePayload from(SongRedis song, LocalDateTime startTime) {
        return NextSongResponsePayload.builder()
                .type(MessageType.NEXT)
                .startTime(startTime)
                .song(song)
                .build();
    }
}
