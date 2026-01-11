package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class UpdateStatusExpiredPaymentException extends GenericException {
    public UpdateStatusExpiredPaymentException() {
        super("Cannot update the status of an expired payment", ErrorCode.INVALID);
    }
}
