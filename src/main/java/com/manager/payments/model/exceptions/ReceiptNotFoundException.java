package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

import java.util.UUID;

public class ReceiptNotFoundException extends GenericException {
    public ReceiptNotFoundException(UUID id) {
        super("Receipt with id " + id + " not found", ErrorCode.NOT_FOUND);
    }
}
