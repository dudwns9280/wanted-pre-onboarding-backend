package com.example.dashboard.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(CommonException exception){
        return ResponseEntity.status(exception.getStatus())
                .body(new ExceptionResponse(exception));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validationExceptionHandler(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ExceptionResponse(new CommonException(ExceptionEnum.VALIDATION_ERROR, message)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> constraintViolationException(ConstraintViolationException ex) {
        System.out.println(ex.getConstraintViolations().toString());

        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(new CommonException(ExceptionEnum.VALIDATION_ERROR, message)));
    }

}
