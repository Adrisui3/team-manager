package com.manager.payments.model.payments;

import java.time.LocalDate;
import java.util.UUID;

public record Payment(UUID id, double amount, String name, String description, LocalDate startDate,
                      LocalDate nextPaymentDate, LocalDate endDate, int periodDays, PaymentStatus status) {
}
