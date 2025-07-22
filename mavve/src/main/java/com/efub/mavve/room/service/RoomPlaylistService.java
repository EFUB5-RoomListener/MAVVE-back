package com.efub.mavve.room.service;

import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.playlist.dto.summary.PlaylistSummary;
import com.efub.mavve.playlist.repository.PlaylistRepository;
import com.efub.mavve.room.domain.Room;
import com.efub.mavve.room.domain.RoomPlaylist;
import com.efub.mavve.room.dto.response.RoomListResponse;
import com.efub.mavve.room.dto.response.RoomResponse;
import com.efub.mavve.room.repository.RoomPlaylistRepository;
import com.efub.mavve.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomPlaylistService {

    private final PlaylistRepository playlistRepository;
    private final RoomRepository roomRepository;
    private final RoomPlaylistRepository roomPlaylistRepository;

    // 방에 플레이리스트 추가
    @Transactional
    public void addPlaylistInRoom(Long roomId, Long playlistId){
        Playlist playlist = getPlaylistOrThrow(playlistId);
        Room room = roomRepository.findByRoomId(roomId)
                .orElseThrow(()-> new MavveException(ExceptionCode.ROOM_NOT_FOUND));

        RoomPlaylist roomPlaylist = RoomPlaylist.create(playlist, room);
        roomPlaylistRepository.save(roomPlaylist);
    }

    // 방의 플레이리스트 조회
    @Transactional(readOnly = true)
    public List<PlaylistSummary> getPlaylistInRoom(Long roomId){
        Room room = roomRepository.findByRoomId(roomId)
                .orElseThrow(()-> new MavveException(ExceptionCode.ROOM_NOT_FOUND));
        List<RoomPlaylist> roomPlaylists = roomPlaylistRepository.findByRoom(room);
        List<Playlist> playlists = roomPlaylists.stream()
                .map(RoomPlaylist::getPlaylist)
                .toList();

//        List<RoomResponse> responseList = roomList.stream()
//                .map(room -> {
//                    int likeCount = roomLikeRepository.countByRoom(room);
//                    return RoomResponse.from(room, likeCount);})
//                .collect(Collectors.toList());
//        return RoomListResponse.from(responseList);

        return playlists.stream()
                .map(PlaylistSummary::from)
                .collect(Collectors.toList());
    }

    // 플레이리스트 존재 여부 검사
    private Playlist getPlaylistOrThrow(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new MavveException(ExceptionCode.PLAYLIST_NOT_FOUND));
    }

    // playlistId 조회
    @Transactional(readOnly = true)
    public Playlist findByPlaylistId(Long playlistId){
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new MavveException(ExceptionCode.PLAYLIST_NOT_FOUND));
    }
}
