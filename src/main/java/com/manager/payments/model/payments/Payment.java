package com.manager.payments.model.payments;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
public record Payment(UUID id, String code, BigDecimal amount, String name, String description, LocalDate startDate,
                      LocalDate endDate, Periodicity periodicity, PaymentStatus status) {

    public Payment update(Double amount, String name, String description, LocalDate startDate, LocalDate endDate,
                          Periodicity periodicity, PaymentStatus status, LocalDate currentDate) {
        PaymentFactory.validateInterval(startDate, endDate, currentDate);
        return toBuilder()
                .amount(BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP))
                .name(name)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .periodicity(periodicity)
                .status(status)
                .build();
    }
}
