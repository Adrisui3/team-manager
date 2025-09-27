package com.manager.shared.exception;

import com.manager.shared.GenericStatus;

public abstract class GenericException extends RuntimeException {
    protected final String message;
    protected final GenericStatus status;

    protected GenericException(String message, GenericStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public GenericStatus getStatus() {
        return status;
    }
}
