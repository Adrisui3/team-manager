package com.manager.payments.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class DisabledPlayerException extends GenericException {
    public DisabledPlayerException() {
        super("Cannot perform action on disabled user.", ErrorCode.INVALID_STATE);
    }
}
