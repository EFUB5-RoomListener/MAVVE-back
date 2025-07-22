package com.efub.mavve.room.payload.summary;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongRedis {
    String songId;
    String spotifyId;
    String title;
    String artist;
    String album;
    String coverUrl;
    int duration;
}