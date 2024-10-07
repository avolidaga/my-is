package ru.se.ifmo.tinder.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(value = {NoEntityWithSuchIdException.class, UserNotFoundException.class})
    public ResponseEntity<?> handleNoSuchEntityExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body("Incorrect request: " + ex);
    }
}
