package com.efub.mavve.diary.repository;


import com.efub.mavve.auth.domain.User;
import com.efub.mavve.diary.domain.Diary;
import com.efub.mavve.diary.domain.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    // DiaryId로 찾기
    Optional<Diary> findByDiaryId(long diaryId);

    // User로 찾기
    Optional<Diary> findByUser(User user);

    // 최신순 한 줄 일기 6개 가져오기
    List<Diary> findTop6ByOrderByCreatedAtDesc();
}
