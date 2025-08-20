package com.manager.payments.model.exceptions;

import java.util.UUID;

public class PlayerNotAssignedException extends RuntimeException {
    public PlayerNotAssignedException(UUID playerId, UUID paymentId) {
        super("Payment " + paymentId + " is not assigned to player " + playerId);
    }
}
