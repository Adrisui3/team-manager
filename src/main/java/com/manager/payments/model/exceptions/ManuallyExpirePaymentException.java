package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class ManuallyExpirePaymentException extends GenericException {
    public ManuallyExpirePaymentException() {
        super("Cannot manually set a payment as expired", ErrorCode.INVALID);
    }
}
