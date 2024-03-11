package com.motorcycleparts.motorcycleparts_master.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleWatchNotFoundException(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
