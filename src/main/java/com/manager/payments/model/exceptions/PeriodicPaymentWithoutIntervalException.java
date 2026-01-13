package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class PeriodicPaymentWithoutIntervalException extends GenericException {
    public PeriodicPaymentWithoutIntervalException() {
        super("A periodic payment must have a valid billing interval", ErrorCode.INVALID);
    }
}
