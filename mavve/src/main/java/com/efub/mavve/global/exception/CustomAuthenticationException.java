package com.efub.mavve.global.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationException extends AuthenticationException {
    private final ExceptionCode exceptionCode;

    public CustomAuthenticationException(ExceptionCode code, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = code;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}

