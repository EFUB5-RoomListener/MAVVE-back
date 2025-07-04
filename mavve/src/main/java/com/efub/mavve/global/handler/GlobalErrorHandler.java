package com.efub.mavve.global.handler;

import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.global.exception.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MavveException.class)
    public ResponseEntity<ExceptionResponse> handleMavveException(MavveException e, HttpServletRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                e.getHttpStatusCode().value(),
                e.getExceptionCodeName(),
                e.getMessage(),
                request.getRequestURI(), ZonedDateTime.now()
        );
        return ResponseEntity.status(e.getHttpStatusCode())
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException runtimeException,
                                                                    HttpServletRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                500,
                ExceptionCode.INTERNAL_SERVER_ERROR.getClientExceptionCode().name(),
                ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(),
                request.getRequestURI(), ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
