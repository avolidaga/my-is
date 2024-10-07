package ru.se.ifmo.tinder.service.exceptions;
public class NoEntityWithSuchIdException extends RuntimeException {
    public NoEntityWithSuchIdException(String name, int id) {
        super("%s with id: %d not found".formatted(name, id));
    }

    public NoEntityWithSuchIdException(String name, String field, int id) {
        super("%s with %s id: %d not found".formatted(name, field, id));
    }
}
