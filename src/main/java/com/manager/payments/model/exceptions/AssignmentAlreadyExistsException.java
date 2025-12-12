package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class AssignmentAlreadyExistsException extends GenericException {
    public AssignmentAlreadyExistsException(UUID playerId, UUID paymentId) {
        super("An assignment already exists between player " + playerId + " and payment " + paymentId,
                ErrorCode.INVALID_STATE);
    }
}
