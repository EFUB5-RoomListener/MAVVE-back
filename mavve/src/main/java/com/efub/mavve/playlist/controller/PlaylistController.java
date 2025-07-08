package com.efub.mavve.playlist.controller;

import com.efub.mavve.auth.service.jwt.CustomUserDetails;
import com.efub.mavve.auth.service.jwt.JwtAuthenticationFilter;
import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.playlist.dto.request.PlaylistCreateRequest;
import com.efub.mavve.playlist.dto.request.PlaylistUpdateRequest;
import com.efub.mavve.playlist.dto.response.PlaylistListResponse;
import com.efub.mavve.playlist.dto.response.PlaylistResponse;
import com.efub.mavve.playlist.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    // 플레이리스트 생성
    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @Valid @RequestBody PlaylistCreateRequest request) {

        Long userId = userDetails.getUser().getUserId();
        Playlist playlist = playlistService.createPlaylist(request, userId);
        PlaylistResponse response = PlaylistResponse.from(playlist);
        return ResponseEntity.created(URI.create("/playlists/" + playlist.getPlaylistId())).body(response);
    }
    @PostMapping("/dev")
    public ResponseEntity<PlaylistResponse> createPlaylistWithoutAuth(@RequestHeader("Auth-Id") Long userId,
                                                                      @Valid @RequestBody PlaylistCreateRequest request) {
        Playlist playlist = playlistService.createPlaylist(request, userId);
        PlaylistResponse response = PlaylistResponse.from(playlist);
        return ResponseEntity.created(URI.create("/playlists/" + playlist.getPlaylistId())).body(response);
    }

    // 플레이리스트 목록 조회
    @GetMapping("/me")
    public ResponseEntity<PlaylistListResponse> getAllPlaylist() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    // 플레이리스트 상세 조회
    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> getPlaylist(@PathVariable("playlistId") Long playlistId) {
        return ResponseEntity.ok(playlistService.getPlaylist(playlistId));
    }
}


