package com.manager.payments.application.exception;

import java.util.UUID;

public class PlayerNotAssignedException extends RuntimeException {
    public PlayerNotAssignedException(UUID playerId, UUID paymentId) {
        super("Payment " + paymentId + " is not assigned to player " + playerId);
    }
}
