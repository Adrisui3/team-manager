package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class ParsingException extends GenericException {
    public ParsingException(String message) {
        super(message, ErrorCode.ERROR);
    }
}
