package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class PlayerNotFoundException extends GenericException {
    public PlayerNotFoundException(UUID id) {
        super("Player with id " + id + " not found", ErrorCode.NOT_FOUND);
    }
}
