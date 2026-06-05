package com.ai.ops.exception;

import com.ai.ops.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {

        return ResponseEntity.internalServerError().
                body(ApiError.builder().timestamp(LocalDateTime.now()).
                        message(ex.getMessage()).path(request.getRequestURI()).build());
    }
}