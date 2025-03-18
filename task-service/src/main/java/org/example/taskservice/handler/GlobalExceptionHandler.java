package org.example.taskservice.handler;

import org.example.taskservice.exception.BaseException;
import org.example.taskservice.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for handling all exceptions thrown across the application.
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleBaseAppException(BaseException e) {
        log.error("Handle application exception: {}", e.getMessage());
        HttpStatus httpStatus = e.getClass().getAnnotation(ResponseStatus.class).code();
        return new ResponseEntity<>(e.getMessage(), httpStatus);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
        log.error("Task not found: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
