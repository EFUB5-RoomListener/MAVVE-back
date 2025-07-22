package com.efub.mavve.room.domain;

import com.efub.mavve.playlist.domain.Playlist;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomPlaylist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public static RoomPlaylist create(Playlist playlist, Room room){
        return RoomPlaylist.builder()
                .playlist(playlist)
                .room(room)
                .build();
    }
}
