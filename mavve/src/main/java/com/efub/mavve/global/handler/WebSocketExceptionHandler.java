package com.efub.mavve.global.handler;

import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.global.exception.dto.ExceptionResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.ZonedDateTime;

@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(MavveException.class)
    @SendToUser("/queue/errors")
    public ExceptionResponse handleMavveException(MavveException e, Message<?> message) {
        return new ExceptionResponse(
                e.getHttpStatusCode().value(),
                e.getExceptionCodeName(),
                e.getMessage(),
                getPath(message),
                ZonedDateTime.now());
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public ExceptionResponse handleException(Exception e, Message<?> message) {
        return new ExceptionResponse(
                500,
                ExceptionCode.INTERNAL_SERVER_ERROR.getClientExceptionCode().name(),
                ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(),
                getPath(message),
                ZonedDateTime.now());
    }

    private String getPath(Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        return accessor.getDestination();
    }
}
