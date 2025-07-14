package com.efub.mavve.songs.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class TotalSongsResponse {
    @NotBlank(message = "검색된 노래가 없습니다.")
    private final List<SongResponse> songs;
    @NotNull(message = "총 개수가 누락되었습니다.")
    private final int totalElements;
    @NotNull(message = "총 페이지 수가 누락되었습니다.")
    private final int totalPages;
    @NotNull(message = "마지막 페이지인지 확인할 수 없습니다.")
    private final boolean last;

    public static TotalSongsResponse of(List<SongResponse> songs, int totalElements, int totalPages, boolean last) {
        return TotalSongsResponse.builder()
                .songs(songs)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .last(last)
                .build();
    }
}
