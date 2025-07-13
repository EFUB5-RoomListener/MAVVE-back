package com.efub.mavve.room.dto.response;

import com.efub.mavve.room.dto.summary.SongSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextSongResponse {
    MessageType type;
    SongSummary song;

    public static NextSongResponse from(SongSummary song) {
        return NextSongResponse.builder()
                .type(MessageType.NEXT)
                .song(song)
                .build();
    }
}
