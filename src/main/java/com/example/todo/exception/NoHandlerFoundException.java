package com.example.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoHandlerFoundException extends Exception {
    private final String message;
}
