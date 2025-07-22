package com.efub.mavve.room.payload.summary;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongRedis {
    String songId;
    String spotifyId;
    String title;
    List<String> artist;
    String album;
    String coverUrl;
    int duration;
}