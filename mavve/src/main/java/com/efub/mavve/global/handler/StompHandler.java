package com.efub.mavve.global.handler;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.service.jwt.CustomUserDetails;
import com.efub.mavve.auth.service.jwt.JwtResolver;
import com.efub.mavve.auth.service.jwt.UserDetailsService;
import com.efub.mavve.global.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtResolver jwtResolver;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // websocket 연결 시 헤더의 jwt token 유효성 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {
            try{
                String bearerToken = accessor.getFirstNativeHeader("Authorization");

                if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                    bearerToken = bearerToken.substring(7);
                    String userId = jwtResolver.resolveAccessToken(bearerToken);

                    CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                    User user = userDetails.getUser();

                    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
                    accessor.setUser(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);   //@AuthenticationPrincipal 어노테이션 사용
                    log.info("websocket connect");
                }
            } catch (CustomAuthenticationException e) {
                throw new MessagingException("STOMP 연결 실패: " + e.getMessage(), e);
            }
        }

        return message;
    }
}
