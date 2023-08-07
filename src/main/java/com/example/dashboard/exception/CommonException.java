package com.example.dashboard.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class CommonException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public CommonException(ExceptionEnum ex) {
        this.status = ex.getStatus();
        this.message = ex.getMessage();
    }

    public CommonException(ExceptionEnum ex, String message) {
        this.status = ex.getStatus();
        this.message = message;
    }
}
