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
    private Long kakaoId;

    @Builder
    public User(String username, Long kakaoId) {
        this.username = username;
        this.kakaoId =  kakaoId;
    }

    public static User fromKakaoInfo(String username, Long kakaoId) {
        return User.builder()
                .username(username)
                .kakaoId(kakaoId)
                .build();
    }
}
