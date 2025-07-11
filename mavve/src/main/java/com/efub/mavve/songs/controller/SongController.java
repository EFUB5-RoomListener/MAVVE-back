package com.efub.mavve.songs.controller;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.songs.dto.response.TotalSongsResponse;
import com.efub.mavve.songs.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @GetMapping("/search")
    public ResponseEntity<TotalSongsResponse> getSongSearchResult(@AuthenticationPrincipal User user,
            @RequestParam String query, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(songService.getSongSearchResults(user, query, page, size));
    }

}
