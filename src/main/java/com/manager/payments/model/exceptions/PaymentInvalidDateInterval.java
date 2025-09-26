package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class PaymentInvalidDateInterval extends GenericException {
    public PaymentInvalidDateInterval() {
        super("Start date cannot be after end date.", GenericStatus.INVALID_STATE);
    }
}
