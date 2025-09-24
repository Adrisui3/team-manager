package com.manager.payments.model.payments;

import com.manager.payments.model.exceptions.PaymentAlreadyExpired;
import com.manager.payments.model.exceptions.PaymentInvalidDateInterval;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

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

        PaymentStatus status = startDate.isAfter(now) ? PaymentStatus.INACTIVE : PaymentStatus.ACTIVE;
        return new Payment(UUID.randomUUID(), amount, name, description, startDate, startDate, endDate, periodDays,
                status, Collections.emptyList());
    }
}
