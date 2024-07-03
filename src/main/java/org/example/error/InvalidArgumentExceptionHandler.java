package org.example.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidArgumentExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected String handleConflict(final IllegalArgumentException e) {
        return e.getMessage();
    }
}
