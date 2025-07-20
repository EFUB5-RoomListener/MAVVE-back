package com.efub.mavve.room.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.dto.projection.RoomLikeCountProjection;
import com.efub.mavve.room.dto.request.RoomCreateRequest;
import com.efub.mavve.room.dto.request.RoomUpdateRequest;
import com.efub.mavve.room.dto.response.RoomListResponse;
import com.efub.mavve.room.dto.response.RoomResponse;
import com.efub.mavve.room.repository.RoomLikeRepository;
import com.efub.mavve.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomLikeRepository roomLikeRepository;

    // 방 생성
    @Transactional
    public RoomResponse createRoom(RoomCreateRequest request, User user){
        Room room = roomRepository.save(request.toEntity(user));
        return RoomResponse.from(room, 0);
    }

    // 방 수정
    @Transactional
    public RoomResponse updateRoom(Long roomId, RoomUpdateRequest request, User user){
        Room room = findByRoomId(roomId);
        authorizeUser(user, room);

        if(request.roomName()!=null){room.changeRoomName(request.roomName());}
        if(request.tag()!=null){room.changeTag(request.tag());}
        room.changeIsPublic(request.isPublic());
        int likeCount = roomLikeRepository.countByRoom(room);

        return RoomResponse.from(room, likeCount);
    }

    // 방 삭제
    @Transactional
    public void deleteRoom(Long roomId, User user){
        Room room = findByRoomId(roomId);
        authorizeUser(user, room); // 방장인지 확인
        roomRepository.delete(room);
    }

    // 공개된 방 리스트 전체 조회
    @Transactional(readOnly = true)
    public RoomListResponse getListRoom() {
        List<Room> roomList = roomRepository.findAll();

        List<RoomResponse> responseList = roomList.stream().filter(Room::isPublic)
                .map(room -> {
                    int likeCount = roomLikeRepository.countByRoom(room);
                    return RoomResponse.from(room, likeCount);})
                .collect(Collectors.toList());

        return RoomListResponse.from(responseList);
    }

    // 내가 만든 방 리스트 조회
    @Transactional(readOnly = true)
    public RoomListResponse getUserListRoom(User user){
        List<Room> roomList = roomRepository.findByUser(user);

        List<RoomResponse> responseList = roomList.stream()
                .map(room -> {
                    int likeCount = roomLikeRepository.countByRoom(room);
                    return RoomResponse.from(room, likeCount);})
                .collect(Collectors.toList());

        return RoomListResponse.from(responseList);
    }

    // 조회순으로 공개된 방 리스트 Top5 조회
    @Transactional(readOnly = true)
    public RoomListResponse getHotListRoom(){
        List<Room> roomList = roomRepository.findTop5ByIsPublicTrueOrderByViewCountDesc();

        List<RoomResponse> responseList = roomList.stream()
                .map(room -> {
                    int likeCount = roomLikeRepository.countByRoom(room);
                    return RoomResponse.from(room, likeCount);})
                .collect(Collectors.toList());
        return RoomListResponse.from(responseList);
    }

    // 좋아요 순으로 TOP5 공개된 방 조회
    @Transactional(readOnly = true)
    public RoomListResponse getLikeListRoom(){
        List<RoomLikeCountProjection> projection = roomRepository.findTop5ByIsPublicTrueOrderByLikeCountDesc();

        List<RoomResponse> responseList = projection.stream()
                .map(proj -> {
                    Room room = findByRoomId(proj.getRoomId());
                    int likeCount = roomLikeRepository.countByRoom(room);
                    return RoomResponse.from(room, likeCount);})
                .collect(Collectors.toList());
        return RoomListResponse.from(responseList);
    }

    // 방 검색
    @Transactional(readOnly = true)
    public RoomListResponse searchRoom(String keyword){
        List<Room> roomList = roomRepository.findByRoomNameContainingIgnoreCaseAndIsPublicTrueOrderByCreatedAtDesc(keyword);

        List<RoomResponse> responseList = roomList.stream()
                .map(room -> {
                    int likeCount = roomLikeRepository.countByRoom(room);
                    return RoomResponse.from(room, likeCount);})
                .collect(Collectors.toList());
        return RoomListResponse.from(responseList);
    }

    // roomId 조회
    @Transactional(readOnly = true)
    public Room findByRoomId(Long roomId){
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
