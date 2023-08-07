package com.example.dashboard.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class ExceptionResponse {
    private HttpStatus status;
    private String message;
    public ExceptionResponse(CommonException ex){
        this.status = ex.getStatus();
        this.message = ex.getMessage();
    }
}
