package com.efub.mavve.global.handler;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.room.service.redis.RoomUserRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final RoomUserRedisService roomUserRedisService;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        Authentication auth = (Authentication) event.getUser();
        if (auth != null && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();

            // sessionId가 저장된 후 종료된 경우
            String sessionId = event.getSessionId();
            if(roomUserRedisService.IfUserSubscribed(sessionId)){
                roomUserRedisService.deleteUser(user, sessionId);
            }
            log.info("WebSocket disconnected: userId={}", user.getUserId());
        }
    }
}

