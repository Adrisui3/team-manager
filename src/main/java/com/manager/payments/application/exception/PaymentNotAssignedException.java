package com.manager.payments.application.exception;

import java.util.UUID;

public class PaymentNotAssignedException extends RuntimeException {
    public PaymentNotAssignedException(UUID userId, UUID paymentId) {
        super("User " + userId + " is not assigned to payment " + paymentId);
    }
}
