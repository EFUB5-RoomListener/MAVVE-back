package com.efub.mavve.diary.dto.request;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.diary.domain.Diary;
import com.efub.mavve.diary.domain.Emoji;
import com.efub.mavve.songs.domain.Song;
import jakarta.validation.constraints.NotNull;

public record DiaryUpdateRequest (Long emojiId,
                                  String spotifySongId,
                                  String comment){
}