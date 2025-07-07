package com.efub.mavve.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClient;

@Configuration
public class OauthClientConfig {

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .build();
    }
}
