package com.efub.mavve.room.payload.request;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteSongRequestPayload {
    List<String> songIds;
}
