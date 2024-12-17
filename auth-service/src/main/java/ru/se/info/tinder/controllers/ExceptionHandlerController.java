package ru.se.info.tinder.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.se.info.tinder.service.exceptions.UserNotFoundException;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.error("Validation error: {}", errors);
        return errors;
    }

    @ExceptionHandler(value = {InvalidDataAccessApiUsageException.class, ValidationException.class})
    public ResponseEntity<?> handleValidationExceptions(RuntimeException ex) {
        log.error("Incorrect request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage());
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<?> handleNoSuchEntityExceptions(RuntimeException ex) {
        log.error("Incorrect request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Incorrect params of request: " + ex.getMessage());
    }
}