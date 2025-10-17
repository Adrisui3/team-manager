package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class PaymentAlreadyExistsException extends GenericException {
    public PaymentAlreadyExistsException(String code) {
        super("Payment with code " + code + " already exists", GenericStatus.INVALID_STATE);
    }
}
