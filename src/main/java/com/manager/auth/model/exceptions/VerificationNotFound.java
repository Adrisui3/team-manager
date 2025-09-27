package com.manager.auth.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class VerificationNotFound extends GenericException {
    public VerificationNotFound() {
        super("Verification code not found", GenericStatus.NOT_FOUND);
    }
}
