package com.efub.mavve.auth.service.oauth;

import com.efub.mavve.auth.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OauthClient {
    private final RestClient restClient;
    private final OauthProperties oauthProperties;

    public SpotifyUserInfoResponse requestOauthInfo(String code){
        SpotifyTokenResponse spotifyTokenResponse = requestToken(code);
        String userInfoRequestUri = oauthProperties.getUserInfoUri();
        String headerName = "Authorization";
        String headerValue = "Bearer " + spotifyTokenResponse.getAccess_token();

        SpotifyUserInfoRaw spotifyUserInfoRaw =  restClient.get()
                                                .uri(userInfoRequestUri)
                                                .header(headerName, headerValue)
                                                .retrieve()
                                                .body(SpotifyUserInfoRaw.class);
        return SpotifyUserInfoResponse.fromSpotifyTokenResponse(spotifyUserInfoRaw.getId(),
                spotifyUserInfoRaw.getDisplay_name(), spotifyUserInfoRaw.getEmail(), spotifyUserInfoRaw.getCountry(), spotifyTokenResponse);
    }

    private SpotifyTokenResponse requestToken(String code){
        String tokenRequestUri = oauthProperties.getTokenRequestUri();
        MultiValueMap<String, String> tokenRequestBody = oauthProperties.createTokenRequestBody(code);

        return restClient.post()
                .uri(tokenRequestUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(tokenRequestBody)
                .retrieve()
                .body(SpotifyTokenResponse.class);
    }

    public String getRedirectUri(){
        return oauthProperties.redirectToSpotify();
    }
}
