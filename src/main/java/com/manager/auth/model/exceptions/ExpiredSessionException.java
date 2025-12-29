package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class ExpiredSessionException extends GenericException {
    public ExpiredSessionException() {
        super("Session expired.", ErrorCode.EXPIRED_TOKEN);
    }
}
