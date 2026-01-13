package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class UniquePaymentWithIntervalException extends GenericException {
    public UniquePaymentWithIntervalException() {
        super("Unique payments must not have a billing interval", ErrorCode.INVALID);
    }
}
