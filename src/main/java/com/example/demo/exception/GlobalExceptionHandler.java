package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UpdateFailedException.class)
    public ResponseEntity<String> handleUpdateFailedException(UpdateFailedException e) {
        e.printStackTrace(); // Log the exception for debugging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Error updating form: " + e.getMessage());
    }

    // Other exception handlers can be added here
}