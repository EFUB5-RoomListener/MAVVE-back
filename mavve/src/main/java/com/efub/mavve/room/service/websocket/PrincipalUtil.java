package com.efub.mavve.room.service.websocket;

import com.efub.mavve.auth.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class PrincipalUtil {
    public User principalToUser(Principal principal){
        User user = (User) ((Authentication) principal).getPrincipal();
        return user;
    }
}
