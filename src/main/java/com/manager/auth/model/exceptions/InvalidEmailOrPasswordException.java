package com.manager.auth.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class InvalidEmailOrPasswordException extends GenericException {
    public InvalidEmailOrPasswordException() {
        super("Invalid email or password.", GenericStatus.UNAUTHORIZED);
    }
}
