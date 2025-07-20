package com.efub.mavve.diary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "emojis")
@NoArgsConstructor(access = PROTECTED)
public class Emoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emojiId;

    @Column(nullable = false)
    private String emojiUrl;
}
