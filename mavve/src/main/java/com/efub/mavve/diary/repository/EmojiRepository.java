package com.efub.mavve.diary.repository;

import com.efub.mavve.diary.domain.Emoji;
import com.efub.mavve.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {
    //EmojiId로 찾기
    Optional<Emoji> findByEmojiId(long emojiId);
}
