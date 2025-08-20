package com.manager.payments.model.exceptions;

public class PaymentAlreadyExpired extends RuntimeException {
    public PaymentAlreadyExpired() {
        super("End date cannot be in the past.");
    }
}
