package com.efub.mavve.songs.service.spotify;

import com.efub.mavve.auth.service.jwt.SpotifyTokenService;
import com.efub.mavve.auth.service.oauth.OauthClient;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.songs.dto.response.spotify.SearchSongResponse;
import com.efub.mavve.songs.dto.response.spotify.SongInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyClient {
    private final RestClient restClient;
    private final SpotifyProperties spotifyProperties;
    private final String headerName = "Authorization";
    private final String headerValue = "Bearer ";
    private final OauthClient oauthClient;
    private final SpotifyTokenService spotifyTokenService;

    public SearchSongResponse getSongSearchResult(String query, String accessToken, String refreshToken, int limit, int offset, String userId) {
        return executeWithRetry((token) -> {
            String searchRequestUri = spotifyProperties.getSerchSongRequestUri(query, limit, offset);
            return restClient.get()
                    .uri(searchRequestUri)
                    .header(headerName, headerValue + token)
                    .retrieve()
                    .body(SearchSongResponse.class);
        }, refreshToken, accessToken, userId, false);
    }

    public SongInfoResponse getSongInfo(String spotifySongId, String refreshToken, String accessToken, String userId) {
        return executeWithRetry((token) -> {
            String songInfoRequestUri = spotifyProperties.getSongInfoRequestUri(spotifySongId);
            SongInfoResponse songInfoResponse = restClient.get()
                    .uri(songInfoRequestUri)
                    .header(headerName, headerValue + accessToken)
                    .retrieve()
                    .body(SongInfoResponse.class);
            if(songInfoResponse.getName() == null){
                throw new MavveException(ExceptionCode.SONG_RESULT_NOT_FOUND);
            }
            return songInfoResponse;
        }, refreshToken, accessToken, userId, false);

    }


    private <T> T executeWithRetry(Function<String, T> requestFunction, String refreshToken, String accessToken, String userId, boolean hasRetried) {
        try {
            return requestFunction.apply(accessToken);
        } catch (HttpClientErrorException.Unauthorized e){
            if(!hasRetried){
                String newAccessToken = oauthClient.getReissueResponse(refreshToken).getAccess_token();
                spotifyTokenService.saveAccessToken(userId, newAccessToken);
                return executeWithRetry(requestFunction, refreshToken, newAccessToken, userId, true);
            } else {
                throw new MavveException(ExceptionCode.SPOTIFY_REISSUE_ERROR);
            }
        }
    }


}
