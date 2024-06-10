package org.example.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(final NoHandlerFoundException e) {
        return new ErrorResponse("Страница не найдена. Проверьте правильность URL-адреса.", "")
                .getMessage();
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String handleSecurityException(final SecurityException e) {
        return new ErrorResponse("Отказано в доступе:\n",
                e.getMessage()).getMessage();
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String hasNotDataException(final NullPointerException e) {
        return new ErrorResponse("Запрашиваемый ресурс не найден или у вас нет необходимых прав. Подробнее:\n",
                e.getMessage()).getMessage();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundInDBException(final NoSuchElementException e) {
        return new ErrorResponse("Ошибка: ", e.getMessage()).getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badArgumentsException(final IllegalArgumentException e) {
        return new ErrorResponse("Не полный или некорректный запрос:\n", e.getMessage()).getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public String unsupportedServerException(final RuntimeException e) {
        return new ErrorResponse("error: ", e.getMessage()).getMessage();
    }
}
