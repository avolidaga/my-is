package ru.se.info.tinder.service.exception;

public class UserNotCompletedRegistrationException extends RuntimeException {
    public UserNotCompletedRegistrationException(String username) {
        super("Current user %s haven't completed registration".formatted(username));
    }
}