package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class PlayerNotFoundException extends GenericException {
    private PlayerNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }

    public static PlayerNotFoundException byId(UUID id) {
        return new PlayerNotFoundException("Player with id " + id + " not found");
    }
}
