package com.efub.mavve.auth.service.oauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

@Component
public class OauthProperties {
    @Getter
    private final String tokenRequestUri;
    @Getter
    private final String userInfoUri;
    private final String redirectUri;
    private final String clientId;

    public OauthProperties(
            @Value("${kakao.token.uri}") String tokenRequestUri,
            @Value("${kakao.user.info.uri}") String userInfoUri,
            @Value("${kakao.client.id}") String clientId,
            @Value("${kakao.redirect.uri}") String redirectUri){
        this.tokenRequestUri = tokenRequestUri;
        this.userInfoUri = userInfoUri;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public MultiValueMap<String, String> createTokenRequestBody(String code){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", redirectUri);
        map.add("client_id", clientId);
        return map;
    }
}
