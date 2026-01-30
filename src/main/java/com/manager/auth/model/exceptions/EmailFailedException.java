package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class EmailFailedException extends GenericException {
    public EmailFailedException(String message) {
        super(message, ErrorCode.ERROR);
    }
}
