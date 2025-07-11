package com.efub.mavve.room.dto.response;

import com.efub.mavve.room.dto.summary.SongSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteSongResponse {
    String type;
    List<String> songIds;

    public static DeleteSongResponse from(List<String> songIds) {
        return DeleteSongResponse.builder()
                .type("DELETE_SONG")
                .songIds(songIds)
                .build();
    }
}
