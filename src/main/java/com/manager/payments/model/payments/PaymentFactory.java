package com.manager.payments.model.payments;

import java.time.LocalDate;
import java.util.Collections;

public class PaymentFactory {

    private PaymentFactory() {
    }

    public static Payment build(double amount, String name, String description, LocalDate startDate,
                                LocalDate endDate, int periodDays) {
        LocalDate now = LocalDate.now();
        if (startDate.isAfter(endDate) || endDate.isBefore(now)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        PaymentStatus status = PaymentStatus.INACTIVE;
        if (startDate.isBefore(now)) {
            status = PaymentStatus.ACTIVE;
        }

        return new Payment(null, amount, name, description, startDate, startDate, endDate, periodDays, status,
                Collections.emptyList());
    }
}
