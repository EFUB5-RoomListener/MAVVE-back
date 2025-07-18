package com.efub.mavve.room.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.domain.RoomLike;
import com.efub.mavve.room.dto.response.RoomLikeResponse;
import com.efub.mavve.room.repository.RoomLikeRepository;
import com.efub.mavve.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomLikeService {

    private final RoomRepository roomRepository;
    private final RoomLikeRepository roomLikeRepository;

    @Transactional
    public RoomLikeResponse roomLike(Long roomId, User user) {
        Room room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new MavveException(ExceptionCode.ROOM_NOT_FOUND));

        Optional<RoomLike> existingLike = roomLikeRepository.findByUserAndRoom(user, room);
        boolean liked;

        if (existingLike.isPresent()) {
            roomLikeRepository.delete(existingLike.get());
            liked = false;
        } else {
            RoomLike like = RoomLike.builder().user(user).room(room).build();
            roomLikeRepository.save(like);
            liked = true;
        }

        int likeCount = roomLikeRepository.countByRoom(room);

        return RoomLikeResponse.builder()
                .roomId(room.getRoomId())
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }

}
