package com.manager.payments.model.payments;

import java.time.LocalDate;
import java.util.UUID;

public record Payment(UUID id, String code, double amount, String name, String description, LocalDate startDate,
                      LocalDate endDate, Periodicity periodicity, PaymentStatus status) {

    public Payment(String code, double amount, String name, String description, LocalDate startDate,
                   LocalDate endDate, Periodicity periodicity, PaymentStatus status) {
        this(null, code, amount, name, description, startDate, endDate, periodicity, status);
    }

    public Payment withStatus(PaymentStatus status) {
        return new Payment(id, code, amount, name, description, startDate, endDate, periodicity, status);
    }
}
