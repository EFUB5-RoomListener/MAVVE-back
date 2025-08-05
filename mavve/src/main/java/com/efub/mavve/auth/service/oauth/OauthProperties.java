package com.efub.mavve.auth.service.oauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Component
public class OauthProperties {
    @Getter
    private final String tokenRequestUri;
    @Getter
    private final String userInfoUri;
    @Getter
    private final String reissueUri;
    private final String localRedirectUri;
    private final String deployRedirectUri;
    private final String authorizeUri;
    private final String clientId;
    private final String clientSecret;

    public OauthProperties(
            @Value("${spotify.token-uri}") String tokenRequestUri,
            @Value("${spotify.user-info-uri}") String userInfoUri,
            @Value("${spotify.client-id}") String clientId,
            @Value("${spotify.redirect-uri.local}") String localRedirectUri,
            @Value("${spotify.redirect-uri.deploy}") String deployRedirectUri,
            @Value("${spotify.auth-uri}") String authorizeUri,
            @Value("${spotify.client-secret}") String clientSecret,
            @Value("${spotify.reissue-uri}") String reissueUri) {
        this.tokenRequestUri = tokenRequestUri;
        this.userInfoUri = userInfoUri;
        this.clientId = clientId;
        this.localRedirectUri = localRedirectUri;
        this.deployRedirectUri = deployRedirectUri;
        this.authorizeUri = authorizeUri;
        this.clientSecret = clientSecret;
        this.reissueUri = reissueUri;
    }

    public String redirectToSpotify() {
        return UriComponentsBuilder.fromUriString(authorizeUri)
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", localRedirectUri)
                .queryParam("scope", "user-read-private user-read-email streaming user-read-playback-state user-modify-playback-state user-read-currently-playing")
                .build().toUriString();
    }

    public MultiValueMap<String, String> createTokenRequestBody(String code, String environment){
        String redirectUri = deployRedirectUri;
        if(environment.equals("dev")){
            redirectUri = localRedirectUri;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", redirectUri);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        return map;
    }

    public MultiValueMap<String, String> createReissueRequestBodyInSpotify(String refreshToken){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", refreshToken);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        return map;
    }
}
