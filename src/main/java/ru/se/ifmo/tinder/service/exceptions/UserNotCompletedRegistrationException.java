package ru.se.ifmo.tinder.service.exceptions;

public class UserNotCompletedRegistrationException extends RuntimeException {
    public UserNotCompletedRegistrationException(String username) {
        super("Current user %s haven't completed registration".formatted(username));
    }
}
