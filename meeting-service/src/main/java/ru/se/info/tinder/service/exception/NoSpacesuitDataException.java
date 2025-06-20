package ru.se.info.tinder.service.exception;

public class NoSpacesuitDataException extends RuntimeException {
    public NoSpacesuitDataException(String username) {
        super("Current user %s haven't created spacesuit request".formatted(username));
    }
}
