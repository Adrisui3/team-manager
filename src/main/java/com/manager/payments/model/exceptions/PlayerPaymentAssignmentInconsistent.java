package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class PlayerPaymentAssignmentInconsistent extends GenericException {
    public PlayerPaymentAssignmentInconsistent(UUID paymentId, UUID playerId) {
        super("Found inconsistent assignment between player with id " + playerId + " and payment with id " + paymentId, GenericStatus.INVALID_STATE);
    }
}
