package com.efub.mavve.global.config;

import com.efub.mavve.global.exception.CustomAuthenticationException;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.dto.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ExceptionCode exceptionCode = ExceptionCode.AUTH_TOKEN_INVALID; // 기본값

        if (authException instanceof CustomAuthenticationException customEx) {
            exceptionCode = customEx.getExceptionCode();
        }

        ExceptionResponse responseBody = new ExceptionResponse(
                HttpStatus.UNAUTHORIZED.value(),
                exceptionCode.name(),
                exceptionCode.getMessage(),
                request.getRequestURI(),
                ZonedDateTime.now()
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), responseBody);
    }

}
