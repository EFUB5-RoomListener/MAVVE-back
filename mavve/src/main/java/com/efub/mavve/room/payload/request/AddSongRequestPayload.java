package com.efub.mavve.room.payload.request;

import com.efub.mavve.room.payload.summary.SongSummary;
import lombok.Getter;

@Getter
public class AddSongRequestPayload {
    SongSummary song;
}