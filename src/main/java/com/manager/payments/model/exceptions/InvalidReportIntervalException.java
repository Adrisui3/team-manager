package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class InvalidReportIntervalException extends GenericException {
    public InvalidReportIntervalException() {
        super("The starting date cannot be after the end date", ErrorCode.INVALID);
    }
}
