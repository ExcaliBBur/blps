package com.example.lab.exception.handler;

import com.example.lab.exception.AlreadyPaidException;
import com.example.lab.exception.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ValidationExceptionsHandler {

    @ExceptionHandler(value = WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleWebExchangeBindException
            (WebExchangeBindException exception) {
        Map<String, List<String>> body = new HashMap<>();
        body.put("errors",
                exception.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .filter(Objects::nonNull)
                        .filter(s -> !s.isBlank())
                        .toList());

        return body;
    }

    @ExceptionHandler(value = AlreadyPaidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleAlreadyPaidException(AlreadyPaidException exception) {
        return Map.of("error", exception.getMessage());
    }

}
