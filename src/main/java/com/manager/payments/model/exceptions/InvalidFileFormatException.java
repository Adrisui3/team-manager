package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class InvalidFileFormatException extends GenericException {
    public InvalidFileFormatException() {
        super("Invalid file format", ErrorCode.INVALID);
    }
}
