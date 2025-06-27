package com.example.demo.controller;

import com.example.demo.exception.ProductAlreadyExistsException;
import com.example.demo.exception.ProductDoesNotExistException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        String errorCode;
        String errorMessage;
    }

    @ExceptionHandler(ProductDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handler(ProductDoesNotExistException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse("400", "Product with given Id does not exist"));
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handler(ProductAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse("400", "Product with given Id already exists"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handler(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("401", "Bad credentials"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handler(Exception exception) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("500", exception.getMessage()));
    }

}
