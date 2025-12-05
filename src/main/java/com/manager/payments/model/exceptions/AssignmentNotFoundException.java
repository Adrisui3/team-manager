package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class AssignmentNotFoundException extends GenericException {
    public AssignmentNotFoundException(UUID playerId, UUID paymentId) {
        super("No assignment exists between player " + playerId + " and payment " + paymentId,
                ErrorCode.INVALID_STATE);
    }
}
