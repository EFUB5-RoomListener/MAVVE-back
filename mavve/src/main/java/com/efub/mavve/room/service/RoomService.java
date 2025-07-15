package com.efub.mavve.room.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.repository.UserRepository;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.dto.request.RoomCreateRequest;
import com.efub.mavve.room.dto.request.RoomUpdateRequest;
import com.efub.mavve.room.dto.response.RoomListResponse;
import com.efub.mavve.room.dto.response.RoomResponse;
import com.efub.mavve.room.repository.RoomRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    // 방 생성
    @Transactional
    public RoomResponse createRoom(@Valid RoomCreateRequest request, User user){
        Room room = roomRepository.save(request.toEntity(user));
        return RoomResponse.from(room);
    }

    // 방 수정
    @Transactional
    public RoomResponse updateRoom(Long roomId, @Valid RoomUpdateRequest request, User user){
        Room room = findByRoomId(roomId);
        authorizeUser(user, room);
        room.changeRoomName(request.roomName());
        room.changeTag(request.tag());
        room.changeIsPublic(request.isPublic());
        return RoomResponse.from(room);
    }
    

    // 방 삭제
    @Transactional
    public void deleteRoom(Long roomId, User user){
        Room room = roomRepository.findByRoomId(roomId).
                orElseThrow(()-> new MavveException(ExceptionCode.ROOM_NOT_FOUND));
        authorizeUser(user, room); // 방장인지 확인
        roomRepository.delete(room);
    }

    // 방 리스트 조회
    @Transactional(readOnly = true)
    public RoomListResponse getListRoom() {
        List<Room> roomList = roomRepository.findAll();

        List<RoomResponse> responseList = roomList.stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());

        return RoomListResponse.from(responseList);
    }

    // 내가 만든 방 리스트 조회
    @Transactional(readOnly = true)
    public RoomListResponse getUserListRoom(User user){
        List<Room> roomList = roomRepository.findByUser(user);

        List<RoomResponse> responseList = roomList.stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());

        return RoomListResponse.from(responseList);
    }

    // 조회순 방 리스트 조회
    @Transactional(readOnly = true)
    public RoomListResponse getHotListRoom(){
        List<Room> roomList = roomRepository.findTop5ByIsPublicTrueOrderByViewCountDesc();

        List<RoomResponse> responseList = roomList.stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());

        return RoomListResponse.from(responseList);
    }

    // userId 조회
    @Transactional(readOnly = true)
    private User findByUserId(Long userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new MavveException(ExceptionCode.USER_NOT_FOUND));
    }

    // roomId 조회
    @Transactional(readOnly = true)
    private Room findByRoomId(Long roomId){
        return roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new MavveException(ExceptionCode.ROOM_NOT_FOUND));
    }

    // 수정, 삭제 권한 확인
    private void authorizeUser(User user, Room room){
        if(!user.getUserId().equals(room.getUser().getUserId())){
            throw new MavveException(ExceptionCode.ROOM_OWNER_MISMATCH);
        }
    }


}
