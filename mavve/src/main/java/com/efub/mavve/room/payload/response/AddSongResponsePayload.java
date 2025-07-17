package com.efub.mavve.room.payload.response;

import com.efub.mavve.room.payload.summary.SongSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddSongResponsePayload {
    MessageType type;
    SongSummary song;

    public static AddSongResponsePayload from(SongSummary song) {
        return AddSongResponsePayload.builder()
                .type(MessageType.ADD_SONG)
                .song(song)
                .build();
    }
}
