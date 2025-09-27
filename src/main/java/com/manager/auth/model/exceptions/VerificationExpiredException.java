package com.manager.auth.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class VerificationExpiredException extends GenericException {
    public VerificationExpiredException() {
        super("Verification code expired", GenericStatus.INVALID_STATE);
    }
}
