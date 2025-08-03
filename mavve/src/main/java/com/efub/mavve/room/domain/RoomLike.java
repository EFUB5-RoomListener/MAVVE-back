package com.efub.mavve.room.domain;

import com.efub.mavve.auth.domain.User;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomLikeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", updatable = false, nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    @Builder
    public RoomLike(Room room, User user) {
        this.room = room;
        this.user = user;
    }

}
