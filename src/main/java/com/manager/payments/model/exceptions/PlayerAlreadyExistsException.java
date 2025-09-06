package com.manager.payments.model.exceptions;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String personalId) {
        super("Player with personal id " + personalId + " already exists");
    }
}
