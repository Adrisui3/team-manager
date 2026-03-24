package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class PlayerAlreadyAssignedToUserException extends GenericException {
    public PlayerAlreadyAssignedToUserException(UUID playerId, UUID userId) {
        super("Player " + playerId + " is already assigned to user " + userId, ErrorCode.INVALID);
    }
}
