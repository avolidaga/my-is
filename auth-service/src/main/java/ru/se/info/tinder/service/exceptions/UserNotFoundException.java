package ru.se.info.tinder.service.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User with username: %s not found".formatted(username));
    }
}
