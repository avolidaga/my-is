package ru.se.ifmo.tinder.service.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User with username: %s not found".formatted(username));
    }
}
