package com.vk.utils;

import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEndpointRequestException.class)
    public ResponseEntity<String> handleInvalidEndpointRequestException(InvalidEndpointRequestException exception) {
        return ResponseEntity.status(500).body(exception.getMessage());
    }
}
