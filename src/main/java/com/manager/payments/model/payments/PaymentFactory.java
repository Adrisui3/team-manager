package com.manager.payments.model.payments;

import com.manager.payments.model.exceptions.PaymentAlreadyExpired;
import com.manager.payments.model.exceptions.PaymentInvalidDateInterval;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentFactory {

    private PaymentFactory() {
    }

    public static Payment build(String code, BigDecimal amount, String name, String description, LocalDate startDate,
                                LocalDate endDate, Periodicity periodicity) {
        LocalDate now = LocalDate.now();
        if (startDate.isAfter(endDate)) {
            throw new PaymentInvalidDateInterval();
        }

        if (endDate.isBefore(now)) {
            throw new PaymentAlreadyExpired();
        }

        PaymentStatus status = startDate.isAfter(now) ? PaymentStatus.INACTIVE : PaymentStatus.ACTIVE;
        return new Payment(code, amount, name, description, startDate, endDate, periodicity, status);
    }
}
