package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class InvalidPasswordChangeException extends GenericException {
    public InvalidPasswordChangeException() {
        super("Password change request does not match authenticated user", ErrorCode.INVALID);
    }
}
