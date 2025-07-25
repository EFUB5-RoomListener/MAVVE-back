package com.efub.mavve.diary.dto.response;

import com.efub.mavve.diary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class DiaryResponse {
    private Long diaryId;
    private String emojiUrl;
    private String nickname;
    private String comment;
    private String songTitle;
    private List<String> songArtist;
    private String songImageUrl;
    private String songDuration;
    private LocalDateTime createdAt;

    public static DiaryResponse from(Diary diary, List<String> artist){
        return DiaryResponse.builder()
                .diaryId(diary.getDiaryId())
                .emojiUrl(diary.getEmoji().getEmojiUrl())
                .nickname(diary.getUser().getUsername())
                .comment(diary.getComment())
                .songTitle(diary.getSong().getTitle())
                .songArtist(artist)
                .songImageUrl(diary.getSong().getCoverImageUrl())
                .songDuration(convertDuration(diary.getSong().getDuration()))
                .createdAt(diary.getCreatedAt())
                .build();
    }

    // duration 변환
    private static String convertDuration(int duration) {
        int totalSeconds = duration/1000;
        int minutes = totalSeconds/60;
        int seconds = totalSeconds%60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}


