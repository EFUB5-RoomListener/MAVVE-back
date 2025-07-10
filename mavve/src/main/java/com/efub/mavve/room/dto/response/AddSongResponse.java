package com.efub.mavve.room.dto.response;

import com.efub.mavve.room.dto.summary.SongSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddSongResponse {
    String type;
    SongSummary song;

    public static AddSongResponse from(SongSummary song) {
        return AddSongResponse.builder()
                .type("ADD_SONG")
                .song(song)
                .build();
    }
}
