package com.efub.mavve.playlist.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.repository.UserRepository;
import com.efub.mavve.auth.service.AuthService;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.playlist.dto.request.PlaylistCreateRequest;
import com.efub.mavve.playlist.dto.request.PlaylistUpdateRequest;
import com.efub.mavve.playlist.dto.response.PlaylistListResponse;
import com.efub.mavve.playlist.dto.response.PlaylistResponse;
import com.efub.mavve.playlist.dto.summary.PlaylistSummary;
import com.efub.mavve.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.efub.mavve.global.exception.ExceptionCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    // 플레이리스트 생성
    @Transactional
    public Playlist createPlaylist(PlaylistCreateRequest request, Long userId) {

        String name = request.name();
        String playImageUrl = request.playImageUrl();

        // 플레이리스트 이름 중복 검사
        if (!playlistRepository.existsByName(name)) {
            throw new MavveException(ExceptionCode.TITLE_ALREADY_EXIST);
        }

        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new MavveException(ExceptionCode.USER_NOT_FOUND));

        Playlist newPlaylist = request.toEntity(user);
        return playlistRepository.save(newPlaylist);
    }

    // 플레이리스트 목록 조회
    @Transactional(readOnly = true)
    public PlaylistListResponse getAllPlaylists() {
        List<PlaylistSummary> playlistSummaries = playlistRepository.findByOrderByPlaylistIdDesc().stream()
                .map(PlaylistSummary::from).toList();
        return new PlaylistListResponse(playlistSummaries);
    }

    // 플레이리스트 상세 조회
    @Transactional(readOnly = true)
    public PlaylistResponse getPlaylist(Long playlistId) {

        // 플레이리스트 존재 여부 검사
        if (!playlistRepository.existsById(playlistId)) {
            throw new MavveException(ExceptionCode.PLAYLIST_NOT_FOUND);
        }

        Playlist playlist = playlistRepository.findByPlaylistId(playlistId);
        return PlaylistResponse.from(playlist);
    }

    // 플레이리스트 수정
    @Transactional
    public void updatePlaylist(Long playlistId, PlaylistUpdateRequest request) {

        String name = request.name();
        String playImageUrl = request.playImageUrl();

        // 플레이리스트 이름 중복 검사
        if (playlistRepository.existsByName(name)) {
            throw new MavveException(ExceptionCode.TITLE_ALREADY_EXIST);
        }

        // 플레이리스트 존재 여부 검사
        if (!playlistRepository.existsById(playlistId)) {
            throw new MavveException(ExceptionCode.PLAYLIST_NOT_FOUND);
        }

        Playlist playlist = playlistRepository.findByPlaylistId(playlistId);
        playlist.changePlaylist(name, playImageUrl);
    }

    // 플레이리스트 삭제
    @Transactional
    public void deletePlaylist(Long playlistId) {

        // 플레이리스트 존재 여부 검사
        if (!playlistRepository.existsById(playlistId)) {
            throw new MavveException(ExceptionCode.PLAYLIST_NOT_FOUND);
        }

        Playlist playlist = playlistRepository.findByPlaylistId(playlistId);
        playlistRepository.delete(playlist);
    }
}
