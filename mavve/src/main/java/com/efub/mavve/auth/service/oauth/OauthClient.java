package com.efub.mavve.auth.service.oauth;

import com.efub.mavve.auth.dto.response.KakaoTokenResponse;
import com.efub.mavve.auth.dto.response.KakaoUserInfoResponse;
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

    public KakaoUserInfoResponse requestOauthInfo(String code){
        KakaoTokenResponse kakaoTokenResponse = requestToken(code);

        String userInfoRequestUri = oauthProperties.getUserInfoUri();
        String headerName = "Authorization";
        String headerValue = "Bearer " + kakaoTokenResponse.getAccess_token();

        return restClient.get()
                .uri(userInfoRequestUri)
                .header(headerName, headerValue)
                .retrieve()
                .body(KakaoUserInfoResponse.class);
    }

    private KakaoTokenResponse requestToken(String code){
        String tokenRequestUri = oauthProperties.getTokenRequestUri();
        MultiValueMap<String, String> tokenRequestBody = oauthProperties.createTokenRequestBody(code);

        return restClient.post()
                .uri(tokenRequestUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(tokenRequestBody)
                .retrieve()
                .body(KakaoTokenResponse.class);
    }
}
