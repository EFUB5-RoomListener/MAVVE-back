package com.efub.mavve.songs.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

// 스포티파이에서 노래 겁색 시 응답 받아오는 용도
@Getter
@RequiredArgsConstructor
public class SearchSongResponse {
    private final Tracks tracks;

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class Tracks {
        private final List<TrackItem> items;
        private final int total;
        private final int offset;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class TrackItem {
        private final String name;
        private final Album album;
        private final List<Artist> artists;
        private final String id;
        private final int duration_ms;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class Album {
        private final String name;
        private final List<Image> images;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class Image {
        private final String url;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class Artist {
        private final String name;
        private final String id;
    }

}
