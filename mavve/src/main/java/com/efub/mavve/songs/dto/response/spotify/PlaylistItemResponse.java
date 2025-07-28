package com.efub.mavve.songs.dto.response.spotify;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PlaylistItemResponse {
    private final List<PlaylistTrackItem> items;
    private final int total;
    private final int offset;

    @Getter
    @RequiredArgsConstructor
    public static class PlaylistTrackItem {
        private final Track track;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Track {
        private final String name;
        private final Album album;
        private final List<Artist> artists;
        private final String id;
        private final int duration_ms;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Album {
        private final String name;
        private final List<Image> images;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Image {
        private final String url;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Artist {
        private final String name;
        private final String id;
    }

    // TODO: 구조 refactor 필요
    public static SearchSongResponse toSearchSongResponse(PlaylistItemResponse playlistResponse) {
        List<SearchSongResponse.TrackItem> convertedTracks = playlistResponse.getItems().stream()
                .map(item -> {
                    Track t = item.getTrack();
                    return SearchSongResponse.TrackItem.builder()
                            .name(t.getName())
                            .id(t.getId())
                            .duration_ms(t.getDuration_ms())
                            .album(SearchSongResponse.Album.builder()
                                    .name(t.getAlbum().getName())
                                    .images(t.getAlbum().getImages().stream()
                                            .map(img -> SearchSongResponse.Image.builder()
                                                    .url(img.getUrl())
                                                    .build())
                                            .toList())
                                    .build())
                            .artists(t.getArtists().stream()
                                    .map(artist -> SearchSongResponse.Artist.builder()
                                            .name(artist.getName())
                                            .id(artist.getId())
                                            .build())
                                    .toList())
                            .build();
                })
                .toList();

        return new SearchSongResponse(
                SearchSongResponse.Tracks.builder()
                        .items(convertedTracks)
                        .total(playlistResponse.getTotal())
                        .offset(playlistResponse.getOffset())
                        .build()
        );
    }
}
