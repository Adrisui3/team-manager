package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class InvalidVerificationCodeException extends GenericException {
    public InvalidVerificationCodeException() {
        super("Invalid verification code", ErrorCode.INVALID_STATE);
    }
}
