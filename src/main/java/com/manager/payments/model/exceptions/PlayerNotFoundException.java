package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class PlayerNotFoundException extends GenericException {
    public PlayerNotFoundException(UUID id) {
        super("Player with id " + id + " not found", GenericStatus.NOT_FOUND);
    }
}
