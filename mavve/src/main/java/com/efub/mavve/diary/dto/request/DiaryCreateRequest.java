package com.efub.mavve.diary.dto.request;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.diary.domain.Diary;
import com.efub.mavve.diary.domain.Emoji;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.songs.domain.Song;
import jakarta.validation.constraints.NotNull;

public record DiaryCreateRequest (@NotNull Long emojiId,
                                  @NotNull String spotifySongId,
                                  @NotNull String comment){
    public Diary toEntity(User user, Emoji emoji, Song song){
        return Diary.builder()
                .user(user)
                .emoji(emoji)
                .song(song)
                .comment(comment())
                .build();
    }
}
