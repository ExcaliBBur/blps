package com.example.lab.exception.handler;

import com.example.lab.exception.IllegalAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class IllegalAccessExceptionHandler {

    @ExceptionHandler(value = IllegalAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleIllegalAccessException(IllegalAccessException exception) {
        return Map.of("error", exception.getMessage());
    }

}
