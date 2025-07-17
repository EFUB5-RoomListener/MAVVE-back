package com.efub.mavve.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class DiaryListResponse {
    private Integer totalCount;
    private List<DiaryResponse> diaryList;

    public static DiaryListResponse from(List<DiaryResponse> diaryList){
        return DiaryListResponse.builder()
                .totalCount(diaryList.size())
                .diaryList(diaryList)
                .build();
    }
}
