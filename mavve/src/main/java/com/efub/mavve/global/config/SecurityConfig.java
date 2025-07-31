package com.efub.mavve.global.config;

import com.efub.mavve.auth.service.jwt.JwtAuthenticationFilter;
import com.efub.mavve.auth.service.jwt.JwtResolver;
import com.efub.mavve.auth.service.jwt.UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtResolver jwtResolver, UserDetailsService userDetailsService) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/**"
                                // TODO: http 메서드 별로 변경 필요
//                                        "/auth/redirect/**",
//                                        "/auth/login",
//                                        "/rooms",
//                                        "/rooms/*/enter",
//                                        "/rooms/*/chats/**",
//                                        "/rooms/hot",
//                                        "/rooms/like",
//                                        "/rooms/*/playlists",
//                                        "/diaries",
//                                        "/playlists/search/**",
//                                        "/image/**",
//                                        "/pub/**",
//                                        "/topic/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtResolver, userDetailsService, customAuthenticationEntryPoint),
                        UsernamePasswordAuthenticationFilter.class
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
