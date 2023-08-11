package com.example.dashboard.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재 합니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "값이 유효하지 않습니다."),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    REDIS_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰 값을 찾을 수 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료 만료되었습니다."),
    MISMATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "refresh 토큰이 정상적이지 않습니다."),
    WRONG_TYPE_TOKEN(HttpStatus.FORBIDDEN, "토큰이 위조/변조 되었습니다. 다시 발급받아주세요"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다");

    private HttpStatus status;
    private String message;

    ExceptionEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
