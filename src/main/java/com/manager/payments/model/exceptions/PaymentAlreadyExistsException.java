package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class PaymentAlreadyExistsException extends GenericException {
    public PaymentAlreadyExistsException(String code) {
        super("Payment with code " + code + " already exists", ErrorCode.ALREADY_EXISTS);
    }
}
