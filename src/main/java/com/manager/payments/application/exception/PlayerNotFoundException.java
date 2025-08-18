package com.manager.payments.application.exception;

import java.util.UUID;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(UUID id) {
        super("Player with id " + id + " not found");
    }
}
