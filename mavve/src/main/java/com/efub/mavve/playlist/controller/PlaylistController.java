package com.efub.mavve.playlist.controller;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.service.jwt.CustomUserDetails;
import com.efub.mavve.auth.service.jwt.JwtAuthenticationFilter;
import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.playlist.dto.request.AddSongRequest;
import com.efub.mavve.playlist.dto.request.PlaylistCreateRequest;
import com.efub.mavve.playlist.dto.request.PlaylistUpdateRequest;
import com.efub.mavve.playlist.dto.response.PlaylistListResponse;
import com.efub.mavve.playlist.dto.response.PlaylistResponse;
import com.efub.mavve.playlist.dto.summary.PlaylistSummary;
import com.efub.mavve.playlist.service.PlaylistService;
import com.efub.mavve.songs.dto.response.SongResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    // 플레이리스트 생성
    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(@AuthenticationPrincipal User user,
                                                @Valid @RequestBody PlaylistCreateRequest request) {

        Playlist playlist = playlistService.createPlaylist(request, user);
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

    // 플레이리스트 수정
    @PatchMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> updatePlaylist(@AuthenticationPrincipal User user,
                                                            @PathVariable("playlistId") Long playlistId,
                                                            @Valid @RequestBody PlaylistUpdateRequest request) {

        playlistService.updatePlaylist(playlistId, request, user);
        return ResponseEntity.ok(playlistService.getPlaylist(playlistId));
    }

    // 플레이리스트 삭제
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@AuthenticationPrincipal User user,
                                                @PathVariable("playlistId") Long playlistId) {

        playlistService.deletePlaylist(playlistId, user);
        return ResponseEntity.ok(Map.of("message", "성공적으로 삭제되었습니다."));
    }

<<<<<<< HEAD
    // 플레이리스트 검색
    @GetMapping("/search")
    public ResponseEntity<List<PlaylistSummary>> searchPlaylists(@RequestParam("keyword") String keyword,
                                                                 @AuthenticationPrincipal User user) {
        List<PlaylistSummary> result = playlistService.searchPlaylists(keyword, user);
        return ResponseEntity.ok(result);
=======
    // 플레이리스트에 노래 추가
    @PostMapping("/{playlistId}/songs")
    public ResponseEntity<SongResponse> addSongInPlaylist(@AuthenticationPrincipal User user,
                                                          @PathVariable("playlistId") Long playlistId,
                                                          @Valid @RequestBody AddSongRequest addSongRequest){
        return ResponseEntity.ok(playlistService.addSongInPlaylist(user, playlistId, addSongRequest));
>>>>>>> develop
    }

}