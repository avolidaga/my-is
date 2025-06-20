package ru.se.info.tinder.service.exception;

public class NoEntityWithSuchIdException extends RuntimeException {
    public NoEntityWithSuchIdException(String name, Long id) {
        super("%s with id: %d not found".formatted(name, id));
    }

}
