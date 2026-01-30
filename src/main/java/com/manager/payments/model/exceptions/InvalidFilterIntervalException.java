package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class InvalidFilterIntervalException extends GenericException {
    public InvalidFilterIntervalException() {
        super("Start and end dates for filtering must be provided together.", ErrorCode.INVALID);
    }
}
