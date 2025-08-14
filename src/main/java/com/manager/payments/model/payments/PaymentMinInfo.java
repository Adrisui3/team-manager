package com.manager.payments.model.payments;

import java.time.LocalDate;
import java.util.UUID;

public record PaymentMinInfo(UUID id, double amount, String name, String description, LocalDate startDate,
                             LocalDate nextPaymentDate, LocalDate endDate, int periodDays, PaymentStatus status) {

    public static PaymentMinInfo from(Payment payment) {
        return new PaymentMinInfo(payment.id(), payment.amount(), payment.name(), payment.description(), payment.startDate(), payment.nextPaymentDate(), payment.endDate(), payment.periodDays(), payment.status());
    }
}
