package com.manager.auth.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class UserAlreadyExists extends GenericException {
    public UserAlreadyExists(String email) {
        super("Email " + email + " already in use", GenericStatus.INVALID_STATE);
    }
}
