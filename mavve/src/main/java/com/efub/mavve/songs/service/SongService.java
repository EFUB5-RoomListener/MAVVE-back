package com.efub.mavve.songs.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.service.jwt.SpotifyTokenService;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.songs.dto.response.spotify.PlaylistItemResponse;
import com.efub.mavve.songs.dto.response.spotify.SearchSongResponse;
import com.efub.mavve.songs.dto.response.SongResponse;
import com.efub.mavve.songs.dto.response.TotalSongsResponse;
import com.efub.mavve.songs.repository.SongRepository;
import com.efub.mavve.songs.service.spotify.SpotifyClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final SpotifyClient spotifyClient;
    private final SpotifyTokenService spotifyTokenService;

    public TotalSongsResponse getSongSearchResults(User user, String query, int page, int size) {
        // 음수면 400 에러
        validatePageNumber(page);
        validatePageSize(size);

        Long userId = user.getUserId();
        log.info("userId - {} is searching : {}", userId, query);


        // 스포티파이 액세스 토큰을 redis에서 가져옴
        String accessToken = spotifyTokenService.getAccessToken(userId.toString());
        String refreshToken = spotifyTokenService.getRefreshToken(userId.toString());

        // 스포티파이 api를 통해 노래 검색
        SearchSongResponse searchSongResponse;
        if(query == null || query.isEmpty()) {
            PlaylistItemResponse playlistItemResponse = spotifyClient.getSongInPlaylist(refreshToken, accessToken, size, page, userId.toString());
            searchSongResponse = PlaylistItemResponse.toSearchSongResponse(playlistItemResponse);
        } else {
            searchSongResponse =  spotifyClient.getSongSearchResult(query, refreshToken, accessToken, size, page, userId.toString());
            System.out.println(searchSongResponse.getTracks().getItems().get(0).getName());
        }

        // 응답 DTO로 변환
        int totalElements = searchSongResponse.getTracks().getTotal();
        throwIfNoSongsFound(totalElements);

        int totalPages = getTotalPages(totalElements, size);
        boolean last = getLast(page, totalPages);

        List<SongResponse> songs = SongResponse.fromSpotifyResponse(searchSongResponse);

        return TotalSongsResponse.of(songs, totalElements, totalPages, last);
    }

    private int getTotalPages(int totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }

    private boolean getLast(int page, int totalPages){
        return page >= totalPages - 1;
    }

    private void throwIfNoSongsFound(int totalElements) {
        if(totalElements < 1) {
            throw new MavveException(ExceptionCode.SONG_NOT_FOUND);
        }
    }

    private void validatePageNumber(int pageNumber){
        if(pageNumber < 0){
            throw new MavveException(ExceptionCode.ILLEGAL_PAGE_NUMBER);
        }
    }

    private void validatePageSize(int pageSize){
        if(pageSize < 0){
            throw new MavveException(ExceptionCode.ILLEGAL_PAGE_SIZE);
        }
    }

}
