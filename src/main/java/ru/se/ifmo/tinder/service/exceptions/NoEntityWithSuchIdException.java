package ru.se.ifmo.tinder.service.exceptions;

import java.util.List;

public class NoEntityWithSuchIdException extends RuntimeException {
    public NoEntityWithSuchIdException(String name, Integer id) {
        super("%s with id: %d not found".formatted(name, id));
    }

    public NoEntityWithSuchIdException(String name, String field, Integer id) {
        super("%s with %s id: %d not found".formatted(name, field, id));
    }

    public NoEntityWithSuchIdException(String name, List<Integer> ids) {
        super("%s with ids: %s not found".formatted(name, ids));
    }
}
