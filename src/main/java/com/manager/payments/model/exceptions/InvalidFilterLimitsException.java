package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class InvalidFilterLimitsException extends GenericException {
    public InvalidFilterLimitsException() {
        super("Start date must be before end date", ErrorCode.INVALID);
    }
}
