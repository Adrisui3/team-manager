package com.manager.payments.model.payments;

import com.manager.payments.model.exceptions.PaymentAlreadyExpired;
import com.manager.payments.model.exceptions.PaymentInvalidDateInterval;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        return Payment.builder()
                .amount(amount.setScale(2, RoundingMode.HALF_UP))
                .code(code)
                .name(name)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .periodicity(periodicity)
                .status(status)
                .build();
    }
}
