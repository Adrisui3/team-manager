package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class EmptyFileException extends GenericException {
    public EmptyFileException() {
        super("File cannot be empty", ErrorCode.INVALID);
    }
}
