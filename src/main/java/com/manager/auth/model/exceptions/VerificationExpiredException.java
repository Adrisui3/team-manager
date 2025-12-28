package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class VerificationExpiredException extends GenericException {
    public VerificationExpiredException() {
        super("Verification code expired", ErrorCode.EXPIRED);
    }
}
