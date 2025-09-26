package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class ReceiptNotFoundException extends GenericException {
    public ReceiptNotFoundException(UUID id) {
        super("Receipt with id " + id + " not found", GenericStatus.NOT_FOUND);
    }
}
