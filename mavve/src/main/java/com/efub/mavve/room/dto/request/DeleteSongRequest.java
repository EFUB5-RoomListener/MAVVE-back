package com.efub.mavve.room.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteSongRequest {
    List<String> songIds;
}
