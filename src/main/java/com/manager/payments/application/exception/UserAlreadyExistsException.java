package com.manager.payments.application.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String personalId) {
        super("User with personal id " + personalId + " already exists");
    }
}
