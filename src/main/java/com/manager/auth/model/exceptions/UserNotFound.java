package com.manager.auth.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class UserNotFound extends GenericException {
    public UserNotFound() {
        super("User not found", GenericStatus.NOT_FOUND);
    }
}
