package com.efub.mavve.songs.dto.response;

import com.efub.mavve.playlist.domain.PlaylistSong;
import com.efub.mavve.songs.domain.Song;
import com.efub.mavve.songs.domain.SongArtist;
import com.efub.mavve.songs.dto.response.spotify.SearchSongResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Getter
@RequiredArgsConstructor
@Builder
public class SongResponse {
    @NotBlank(message = "노래 id가 누락되었습니다.")
    private final String spotifySongId;
    @NotBlank(message = "노래 제목이 누락되었습니다.")
    private final String title;
    @NotBlank(message = "가수가 누락되었습니다.")
    private final List<String> artist;
    @NotBlank(message = "앨범이 누락되었습니다.")
    private final String album;
    @NotBlank(message = "커버이미지가 누락되었습니다.")
    private final String coverUrl;
    @NotBlank(message = "곡의 총 길이가 누락되었습니다.")
    private final int duration;

    private final LocalDateTime createdAt;

    public static List<SongResponse> fromSpotifyResponse(SearchSongResponse searchSongResponse) {
        return searchSongResponse.getTracks().getItems().stream()
                .map(trackItem -> SongResponse.builder()
                        .spotifySongId(trackItem.getId())
                        .title(trackItem.getName())
                        .artist(trackItem.getArtists().stream().map(SearchSongResponse.Artist::getName).toList())
                        .album(trackItem.getAlbum().getName())
                        .coverUrl(trackItem.getAlbum().getImages().get(0).getUrl())
                        .duration(trackItem.getDuration_ms())
                        .build()).toList();
    }

    public static SongResponse fromSongEntity(PlaylistSong playlistSong, Song song) {
        return SongResponse.builder()
                .spotifySongId(song.getSpotifySongId())
                .album(song.getAlbum())
                .title(song.getTitle())
                .duration(song.getDuration())
                .coverUrl(song.getCoverImageUrl())
                .artist(song.getSongArtists().stream().map(songArtist -> songArtist.getArtist().getArtistName()).toList())
                .createdAt(playlistSong.getCreatedAt())
                .build();
    }
}
