package com.app.RouteOptimizer.exception;

import com.app.RouteOptimizer.utils.ErrorResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(HubAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> hubAlreadyExistsExceptionHandler(HubAlreadyExistsException hex){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                hex.getMessage(),
                "The location code provided is already in use"
        );
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(409));// CONFLICT error is for "already in use"
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException mex){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                mex.getMessage(),
                "Incorrect request"
        );
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(HubDoesNotExistsException.class)
    public ResponseEntity<ErrorResponse> hubDoesNotExistsHandler(HubDoesNotExistsException hdex){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                hdex.getMessage(),
                "Hub provided is not registered"
        );
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(400));
    }

}