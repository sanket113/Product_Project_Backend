package com.example.rbac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidOtpException.class,
            OtpExpiredException.class,
            OtpAttemptsExceededException.class
    })
    public ResponseEntity<?> handleOtpExceptions(RuntimeException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Log the exception in terminal
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "message", ex.getMessage() // optional: show real message
                ));
    }


    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, ex.getStatus());
    }
}
