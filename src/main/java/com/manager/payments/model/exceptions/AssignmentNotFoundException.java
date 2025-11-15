package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class AssignmentNotFoundException extends GenericException {
    public AssignmentNotFoundException(UUID playerId, UUID paymentId) {
        super("No assignment already exists between player " + playerId + " and payment " + paymentId,
                GenericStatus.INVALID_STATE);
    }
}
