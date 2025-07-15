package com.efub.mavve.auth.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    private String userImageUrl;

    @Column(nullable = false)
    private String spotifyUserId;

    @Builder
    public User(String username, String spotifyUserId) {
        this.username = username;
        this.spotifyUserId = spotifyUserId;
    }

    public static User fromSpotifyInfo(String spotifyNickname, String spotifyUserId) {
        return User.builder()
                .username(spotifyNickname)
                .spotifyUserId(spotifyUserId)
                .build();
    }

    public void updateProfile(String username, String profileImageUrl){
        this.username = username;
        this.userImageUrl = profileImageUrl;
    }
}
