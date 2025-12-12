package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class VerificationEmailFailedException extends GenericException {
    public VerificationEmailFailedException(String message) {
        super(message, ErrorCode.ERROR);
    }
}
