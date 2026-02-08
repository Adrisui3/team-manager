package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class InvalidReceiptAmountException extends GenericException {
    public InvalidReceiptAmountException() {
        super("A receipt cannot have a negative amount", ErrorCode.INVALID);
    }
}
