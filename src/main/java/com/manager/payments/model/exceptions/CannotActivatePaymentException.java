package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class CannotActivatePaymentException extends GenericException {
    public CannotActivatePaymentException() {
        super("Cannot activate a payment outside of its billing period", ErrorCode.INVALID);
    }
}
