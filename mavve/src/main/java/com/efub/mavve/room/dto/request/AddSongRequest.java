package com.efub.mavve.room.dto.request;

import com.efub.mavve.room.dto.summary.SongSummary;
import lombok.Getter;

@Getter
public class AddSongRequest{
    SongSummary song;
}