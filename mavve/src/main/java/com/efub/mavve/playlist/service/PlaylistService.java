package com.efub.mavve.playlist.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.repository.UserRepository;
import com.efub.mavve.auth.service.AuthService;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.playlist.dto.request.PlaylistCreateRequest;
import com.efub.mavve.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.efub.mavve.global.exception.ExceptionCode;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    @Transactional
    public Playlist createPlaylist(PlaylistCreateRequest request, Long userId) throws MavveException {

        String name = request.name();
        String playImageUrl = request.playImageUrl();

        // 플레이리스트 이름 중복 검사
        if (playlistRepository.existsByName(name)) {
            throw new MavveException(ExceptionCode.TITLE_ALREADY_EXIST);
        }

        User user = userRepository.findByKakaoId(userId)
                .orElseThrow(() -> new MavveException(ExceptionCode.USER_NOT_FOUND));

        Playlist newPlaylist = request.toEntity(user);
        return playlistRepository.save(newPlaylist);
    }
}
