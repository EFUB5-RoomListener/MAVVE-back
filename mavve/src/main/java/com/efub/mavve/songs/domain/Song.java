package com.efub.mavve.songs.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long songId;

    @Column(nullable = false)
    Long spotifySongId;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String artist;

    @Column(nullable = false)
    int duration;

    @Column(nullable = false)
    String coverImageUrl;
}
