package com.example.lab.exception.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class SqlExceptionHandler {
    private final Map<String, String> violationsMap;

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {

        String message = violationsMap.entrySet().stream()
                .filter(entry -> exception.getMessage().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny()
                .orElse("Возникло непредвиденное исключение");

        return Map.of("error", message);
    }

}
