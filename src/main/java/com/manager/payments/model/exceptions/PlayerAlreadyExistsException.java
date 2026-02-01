package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class PlayerAlreadyExistsException extends GenericException {
    private PlayerAlreadyExistsException(String message) {
        super(message, ErrorCode.ALREADY_EXISTS);
    }

    public static PlayerAlreadyExistsException byPersonalId(String personalId) {
        return new PlayerAlreadyExistsException("Player with personal id " + personalId + " already exists");
    }
}
