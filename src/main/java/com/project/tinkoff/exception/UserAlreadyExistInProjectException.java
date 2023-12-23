package com.project.tinkoff.exception;

public class UserAlreadyExistInProjectException extends RuntimeException {
    public UserAlreadyExistInProjectException(String message) {
        super(message);
    }
}
