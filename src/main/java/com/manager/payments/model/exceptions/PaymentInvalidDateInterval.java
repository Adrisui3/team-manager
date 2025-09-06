package com.manager.payments.model.exceptions;

public class PaymentInvalidDateInterval extends RuntimeException {
    public PaymentInvalidDateInterval() {
        super("Start date cannot be after end date.");
    }
}
