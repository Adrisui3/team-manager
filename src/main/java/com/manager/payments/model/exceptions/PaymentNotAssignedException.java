package com.manager.payments.model.exceptions;

import java.util.UUID;

public class PaymentNotAssignedException extends RuntimeException {
    public PaymentNotAssignedException(UUID userId, UUID paymentId) {
        super("User " + userId + " is not assigned to payment " + paymentId);
    }
}
