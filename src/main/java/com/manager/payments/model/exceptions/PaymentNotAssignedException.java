package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class PaymentNotAssignedException extends GenericException {
    public PaymentNotAssignedException(UUID userId, UUID paymentId) {
        super("User " + userId + " is not assigned to payment " + paymentId, GenericStatus.INVALID_STATE);
    }
}
