package com.manager.auth.model.exceptions;

import com.manager.shared.ErrorCode;
import com.manager.shared.exception.GenericException;

public class EqualNewPasswordException extends GenericException {
    public EqualNewPasswordException() {
        super("New and old password must be different", ErrorCode.INVALID_STATE);
    }
}
