package com.efub.mavve.room.payload.response;

import com.efub.mavve.room.payload.summary.SongRedis;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextSongResponsePayload {
    MessageType type;
    SongRedis song;

    public static NextSongResponsePayload from(SongRedis song) {
        return NextSongResponsePayload.builder()
                .type(MessageType.NEXT)
                .song(song)
                .build();
    }
}
