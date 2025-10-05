package com.manager.payments.model.exceptions;

import com.manager.shared.GenericStatus;
import com.manager.shared.exception.GenericException;

public class DisabledPlayerException extends GenericException {
    public DisabledPlayerException() {
        super("Cannot perform action on disabled user.", GenericStatus.INVALID_STATE);
    }
}
