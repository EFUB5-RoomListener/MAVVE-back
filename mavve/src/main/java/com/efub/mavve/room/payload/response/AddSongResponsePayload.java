package com.efub.mavve.room.payload.response;

import com.efub.mavve.room.payload.summary.SongRedis;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddSongResponsePayload {
    MessageType type;
    SongRedis song;

    public static AddSongResponsePayload from(SongRedis song) {
        return AddSongResponsePayload.builder()
                .type(MessageType.ADD_SONG)
                .song(song)
                .build();
    }
}
