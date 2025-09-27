package com.manager.auth.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class DisabledUserException extends GenericException {
    public DisabledUserException() {
        super("User account disabled.", GenericStatus.FORBIDDEN);
    }
}
