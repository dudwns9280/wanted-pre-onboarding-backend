package com.example.dashboard.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재 합니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "값이 유효하지 않습니다."),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다");

    private HttpStatus status;
    private String message;

    ExceptionEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
