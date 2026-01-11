package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class UserNotFound extends GenericException {
    private UserNotFound(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }

    public static UserNotFound byId(UUID id) {
        return new UserNotFound(String.format("User with id %s not found", id));
    }

    public static UserNotFound byEmail(String email) {
        return new UserNotFound(String.format("User with email %s not found", email));
    }
}
