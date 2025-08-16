package com.manager.payments.application.exception;

import java.util.UUID;

public class UserNotAssignedException extends RuntimeException {
    public UserNotAssignedException(UUID userId, UUID paymentId) {
        super("Payment " + paymentId + " is not assigned to user " + userId);
    }
}
