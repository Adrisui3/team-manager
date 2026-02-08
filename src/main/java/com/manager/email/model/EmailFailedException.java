package com.manager.email.model;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class EmailFailedException extends GenericException {
    public EmailFailedException(String message) {
        super(message, ErrorCode.ERROR);
    }
}
