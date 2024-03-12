package com.vk.utils;

import com.vk.security.exception.UsernameAlreadyExistsException;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEndpointRequestException.class)
    public ResponseEntity<String> handleInvalidEndpointRequestException(InvalidEndpointRequestException exception) {
        return ResponseEntity.status(500).body(exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception) {
        return ResponseEntity.status(409).body(exception.getMessage());
    }
}
