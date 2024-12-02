package ru.se.info.tinder.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({MethodArgumentNotValidException.class, InvalidDataAccessApiUsageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.error("Validation error: {}", errors);
        return Mono.just(errors);
    }

    @ExceptionHandler(value = {NoEntityWithSuchIdException.class})
    public Mono<ResponseEntity<String>> handleNoSuchEntityExceptions(RuntimeException ex) {
        log.error("Incorrect request: {}", ex.getMessage());
        return Mono.just(ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage()));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public Mono<ResponseEntity<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return Mono.just(ResponseEntity.badRequest().body("Incorrect params of request: " + ex.getMessage()));
    }
}
