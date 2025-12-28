package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class PaymentAlreadyExpired extends GenericException {
    public PaymentAlreadyExpired() {
        super("End date cannot be in the past.", ErrorCode.EXPIRED);
    }
}
