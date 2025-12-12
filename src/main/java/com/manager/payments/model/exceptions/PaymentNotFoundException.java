package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class PaymentNotFoundException extends GenericException {
    public PaymentNotFoundException(UUID id) {
        super("Payment not found with id: " + id, ErrorCode.NOT_FOUND);
    }
}
