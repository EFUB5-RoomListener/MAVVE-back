package com.efub.mavve.room.payload.summary;

import com.efub.mavve.room.service.redis.RoomSongRedisService;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SongSummary {
    String spotifyId;
    String title;
    String artist;
    String album;
    String coverUrl;
    int duration;

    public static SongRedis toRedisPOJO(SongSummary songSummary){
        return SongRedis.builder()
                .songId(RoomSongRedisService.createUUID())
                .spotifyId(songSummary.spotifyId)
                .title(songSummary.title)
                .artist(songSummary.artist)
                .album(songSummary.album)
                .coverUrl(songSummary.coverUrl)
                .duration(songSummary.duration)
                .build();
    }
}