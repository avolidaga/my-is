package ru.se.info.tinder.service.exception;

import java.util.List;

public class NoEntityWithSuchIdException extends RuntimeException {
    public NoEntityWithSuchIdException(String name, Long id) {
        super("%s with id: %d not found".formatted(name, id));
    }

    public NoEntityWithSuchIdException(String name, String field, Long id) {
        super("%s with %s id: %d not found".formatted(name, field, id));
    }

    public NoEntityWithSuchIdException(String name, List<Long> ids) {
        super("%s with ids: %s not found".formatted(name, ids));
    }
}
