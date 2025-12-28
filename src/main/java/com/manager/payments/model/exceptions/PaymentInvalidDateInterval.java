package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class PaymentInvalidDateInterval extends GenericException {
    public PaymentInvalidDateInterval() {
        super("Start date cannot be after end date.", ErrorCode.INVALID);
    }
}
