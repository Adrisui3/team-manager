package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class PlayerAlreadyExistsException extends GenericException {
    public PlayerAlreadyExistsException(String personalId) {
        super("Player with personal id " + personalId + " already exists", ErrorCode.ALREADY_EXISTS);
    }
}
