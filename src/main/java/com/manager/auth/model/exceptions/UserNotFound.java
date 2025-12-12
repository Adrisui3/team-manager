package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class UserNotFound extends GenericException {
    public UserNotFound() {
        super("User not found", ErrorCode.NOT_FOUND);
    }
}
