package com.manager.payments.model.exceptions;

import java.util.UUID;

public class PlayerPaymentAssignmentInconsistent extends RuntimeException {
    public PlayerPaymentAssignmentInconsistent(UUID paymentId, UUID playerId) {
        super("Found inconsistent assignment between player with id " + playerId + " and payment with id " + paymentId);
    }
}
