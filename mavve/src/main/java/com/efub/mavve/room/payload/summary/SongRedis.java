package com.efub.mavve.room.payload.summary;

import com.efub.mavve.room.service.redis.RoomSongRedisService;
import com.efub.mavve.songs.domain.Song;
import com.efub.mavve.songs.dto.response.spotify.SearchSongResponse;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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

    public static SongRedis EntityToRedisPOJO(Song song){
        return SongRedis.builder()
                .songId(RoomSongRedisService.createUUID())
                .spotifyId(song.getSpotifySongId())
                .title(song.getTitle())
                .artist(song.getSongArtists().stream()
                        .map(songArtist -> songArtist.getArtist().getArtistName())
                        .collect(Collectors.toList()))
                .album(song.getAlbum())
                .coverUrl(song.getCoverImageUrl())
                .duration(song.getDuration())
                .build();
    }
}