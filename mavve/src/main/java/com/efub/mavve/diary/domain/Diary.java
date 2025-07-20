package com.efub.mavve.diary.domain;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.songs.domain.Song;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "diaries")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @ManyToOne
    @JoinColumn(name = "emojiId", nullable = false)
    private Emoji emoji;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "songId", nullable = false)
    private Song song;

    @Column(nullable = false)
    private String comment;

    @CreatedDate
    private LocalDateTime createdAt;

    // 빌더
    @Builder
    public Diary(User user, Emoji emoji, Song song, String comment) {
        this.user = user;
        this.emoji = emoji;
        this.song = song;
        this.comment = comment;
    }

    // 이모지 수정
    public void changeEmojiId(Emoji emoji){this.emoji = emoji;}

    // 노래 수정
    public void changeSongId(Song song){this.song = song;}

    // 내용 수정
    public void changeComment(String comment){this.comment = comment;}

}
