package com.manager.shared.exception;

import com.manager.shared.ErrorCode;

public abstract class GenericException extends RuntimeException {
    protected final String message;
    protected final ErrorCode status;

    protected GenericException(String message, ErrorCode status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ErrorCode getStatus() {
        return status;
    }
}
