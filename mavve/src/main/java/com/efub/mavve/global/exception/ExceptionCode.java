package com.efub.mavve.global.exception;

import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    // 전체
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ClientExceptionCode.INTERNAL_SERVER_ERROR, "예상치 못한 서버에러가 발생했습니다."),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, ClientExceptionCode.ILLEGAL_ARGUMENT, "필수 파라미터 누락"),

    // 유저
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, ClientExceptionCode.LOGIN_ERROR, "카카오 인증에 실패했습니다."),
    AUTH_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ClientExceptionCode.AUTH_SERVER_ERROR, "카카오 서버와 통신하는 과정에서 예상치 못한 에러가 발생했습니다."),
    AUTH_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, ClientExceptionCode.AUTH_TOKEN_EMPTY, "다시 로그인해주세요."),
    ACCESS_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, ClientExceptionCode.ACCESS_TOKEN_EMPTY, "엑세스 토큰이 존재하지 않습니다. 다시 로그인해주세요."),
    REFRESH_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, ClientExceptionCode.REFRESH_TOKEN_EMPTY, "리프레시 토큰이 존재하지 않습니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ClientExceptionCode.AUTH_TOKEN_EXPIRED, "만료된 토큰입니다."),
    AUTH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, ClientExceptionCode.AUTH_TOKEN_INVALID, "올바르지 않은 토큰 정보입니다."),
    AUTH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, ClientExceptionCode.AUTH_TOKEN_MISMATCH, "액세스 토큰과 리프레시 토큰의 소유자가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.USER_NOT_FOUND, "찾을 수 없는 유저입니다."),
    ALREADY_LOGGED_TOKEN(HttpStatus.UNAUTHORIZED, ClientExceptionCode.ALREADY_LOGGED_TOKEN, "로그아웃된 토큰입니다."),

    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.ROOM_NOT_FOUND, "해당되는 방이 존재하지 않습니다."),

    // 노래
    SONG_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.SONG_NOT_FOUND, "해당되는 노래가 존재하지 않습니다."),
    SONG_LIST_EMPTY(HttpStatus.NO_CONTENT, ClientExceptionCode.SONG_LIST_EMPTY, "해당 방의 노래리스트가 비어있습니다."),
    ILLEGAL_PAGE_NUMBER(HttpStatus.BAD_REQUEST, ClientExceptionCode.ILLEGAL_PAGE_NUMBER, "잘못된 페이지 넘버입니다."),
    ILLEGAL_PAGE_SIZE(HttpStatus.BAD_REQUEST, ClientExceptionCode.ILLEGAL_PAGE_SIZE, "잘못된 페이지 사이즈입니다."),
    SONG_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.SONG_RESULT_NOT_FOUND, "스포티파이에서 해당 노래에 대한 검색결과가 존재하지 않습니다."),

    //플레이리스트
    PLAYLIST_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.PLAYLIST_NOT_FOUND, "해당되는 플레이리스트가 존재하지 않습니다."),
    TITLE_ALREADY_EXIST(HttpStatus.CONFLICT, ClientExceptionCode.TITLE_ALREADY_EXIST, "이미 존재하는 이름의 플레이리스트입니다."),

    //이미지
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ClientExceptionCode.IMAGE_UPLOAD_FAILED, "이미지 업로드에 실패하였습니다."),
    NO_FILE_PROVIDED(HttpStatus.BAD_REQUEST, ClientExceptionCode.NO_FILE_PROVIDED, "이미지가 존재하지 않습니다."),
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, ClientExceptionCode.INVALID_IMAGE_EXTENSION, "이미지의 확장자가 jpg, jpeg, png, webp 중 하나가 아닙니다."),

    //redis
    REDIS_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ClientExceptionCode.REDIS_SAVE_ERROR, "노래 저장 중 예상치 못한 에러가 발생했습니다."),
    REDIS_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ClientExceptionCode.REDIS_DELETE_ERROR, "노래 삭제 중 예상치 못한 에러가 발생했습니다."),
    REDIS_DESERIALIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ClientExceptionCode.REDIS_DESERIALIZATION_ERROR, "Redis값 역직렬화에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final ClientExceptionCode clientExceptionCode;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, ClientExceptionCode clientExceptionCode, String message) {
        this.httpStatus = httpStatus;
        this.clientExceptionCode = clientExceptionCode;
        this.message = message;
    }
}
