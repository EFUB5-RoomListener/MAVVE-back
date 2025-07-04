package com.efub.mavve.global.exception;

import org.springframework.http.HttpStatusCode;

public class MavveException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public MavveException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    @Override
    public String getMessage() {
        return exceptionCode.getMessage();
    }

    public HttpStatusCode getHttpStatusCode() {
        return exceptionCode.getHttpStatus();
    }

    public String getExceptionCodeName(){
        return exceptionCode.getClientExceptionCode().name();
    }
}
