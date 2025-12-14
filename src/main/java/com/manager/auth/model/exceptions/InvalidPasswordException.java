package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class InvalidPasswordException extends GenericException {
    public InvalidPasswordException(String message) {
        super(message, ErrorCode.INVALID_STATE);
    }
}
