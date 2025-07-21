package com.efub.mavve.room.payload.response;

import com.efub.mavve.room.payload.summary.SongSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextSongResponsePayload {
    MessageType type;
    SongSummary song;

    public static NextSongResponsePayload from(SongSummary song) {
        return NextSongResponsePayload.builder()
                .type(MessageType.NEXT)
                .song(song)
                .build();
    }
}
