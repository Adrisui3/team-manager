package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class PlayerNotAssignedException extends GenericException {
    public PlayerNotAssignedException(UUID playerId, UUID paymentId) {
        super("Payment " + paymentId + " is not assigned to player " + playerId, GenericStatus.INVALID_STATE);
    }
}
