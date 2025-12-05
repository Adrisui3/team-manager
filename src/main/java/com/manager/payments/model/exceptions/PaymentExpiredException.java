package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class PaymentExpiredException extends GenericException {
    public PaymentExpiredException() {
        super("Cannot perform action on expired payment", ErrorCode.INVALID_STATE);
    }
}
