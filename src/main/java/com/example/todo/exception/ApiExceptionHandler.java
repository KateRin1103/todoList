package com.example.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException validationException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(validationException.getMessage())
                .status(BAD_REQUEST)
                .timestamp(now())
                .build(),
                BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException notFoundException) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(notFoundException.getMessage())
                .status(NOT_FOUND)
                .timestamp(now())
                .build(),
                NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundError() {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message("Incorrect path")
                .status(NOT_FOUND)
                .timestamp(now())
                .build(),
                NOT_FOUND);
    }
}
