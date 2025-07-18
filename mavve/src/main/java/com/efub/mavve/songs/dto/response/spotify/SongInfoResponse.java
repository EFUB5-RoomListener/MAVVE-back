package com.efub.mavve.songs.dto.response.spotify;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SongInfoResponse {
    private final Album album;
    private final List<Artist> artists;
    private final int duration_ms;
    private final String id;
    private final String name;

    @Getter
    @RequiredArgsConstructor
    public static class Album {
        private final String name;
        private final List<Image> images;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Image{
        private final String url;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Artist {
        private final String id;
        private final String name;
    }
}
