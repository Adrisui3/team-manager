package com.manager.payments.model.payments;

import com.manager.payments.model.exceptions.PaymentAlreadyExpired;
import com.manager.payments.model.exceptions.PaymentInvalidDateInterval;

import java.time.LocalDate;
import java.util.Collections;

public class PaymentFactory {

    private PaymentFactory() {
    }

    public static Payment build(double amount, String name, String description, LocalDate startDate,
                                LocalDate endDate, int periodDays) {
        LocalDate now = LocalDate.now();
        if (startDate.isAfter(endDate)) {
            throw new PaymentInvalidDateInterval();
        }

        if (endDate.isBefore(now)) {
            throw new PaymentAlreadyExpired();
        }

        PaymentStatus status = PaymentStatus.INACTIVE;
        if (startDate.isBefore(now)) {
            status = PaymentStatus.ACTIVE;
        }

        return new Payment(null, amount, name, description, startDate, startDate, endDate, periodDays, status,
                Collections.emptyList());
    }
}
