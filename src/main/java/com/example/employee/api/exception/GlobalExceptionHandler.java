package com.example.employee.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.employee.api.response.ServerResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException e) {

        return ResponseEntity.badRequest()
                .body(new ServerResponse<Object>(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerResponse<Object>> handleException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ServerResponse<Object>(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal server error",
                        null
                ));
        
    }
    
    @ExceptionHandler(LeadException.class)
    public ResponseEntity<ServerResponse<Object>> handleLeadException(
            LeadException e) {

        return ResponseEntity.badRequest()
                .body(new ServerResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        null
                ));
    }
}