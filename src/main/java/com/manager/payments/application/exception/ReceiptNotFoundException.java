package com.manager.payments.application.exception;

import java.util.UUID;

public class ReceiptNotFoundException extends RuntimeException {
    public ReceiptNotFoundException(UUID id) {
        super("Receipt with id " + id + " not found");
    }
}
