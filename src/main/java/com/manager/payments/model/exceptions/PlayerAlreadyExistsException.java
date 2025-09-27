package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class PlayerAlreadyExistsException extends GenericException {
    public PlayerAlreadyExistsException(String personalId) {
        super("Player with personal id " + personalId + " already exists", GenericStatus.INVALID_STATE);
    }
}
