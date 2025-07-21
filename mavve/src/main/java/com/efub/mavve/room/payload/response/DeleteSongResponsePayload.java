package com.efub.mavve.room.payload.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteSongResponsePayload {
    MessageType type;
    List<String> songIds;

    public static DeleteSongResponsePayload from(List<String> songIds) {
        return DeleteSongResponsePayload.builder()
                .type(MessageType.DELETE_SONG)
                .songIds(songIds)
                .build();
    }
}
