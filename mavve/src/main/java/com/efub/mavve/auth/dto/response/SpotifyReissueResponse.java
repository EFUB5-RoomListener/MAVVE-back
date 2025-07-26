package com.efub.mavve.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class SpotifyReissueResponse {
   String access_token;
   String token_type;
   int expires_in;
   String refresh_token;
   String scope;
}
