package com.efub.mavve.room.payload.summary;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SongSummary {
    String songId;
    String title;
    String artist;
    String album;
    String coverUrl;
    int duration;
}