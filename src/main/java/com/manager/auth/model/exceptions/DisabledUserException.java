package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class DisabledUserException extends GenericException {
    public DisabledUserException() {
        super("User account disabled.", ErrorCode.FORBIDDEN);
    }
}
