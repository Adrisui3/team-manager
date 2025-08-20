package com.manager.payments.model.exceptions;

import java.util.UUID;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(UUID id) {
        super("Player with id " + id + " not found");
    }
}
