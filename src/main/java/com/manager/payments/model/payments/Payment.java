package com.manager.payments.model.payments;

import com.manager.payments.model.users.UserMinInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Payment(UUID id, double amount, String name, String description, LocalDate startDate,
                      LocalDate nextPaymentDate, LocalDate endDate, int periodDays, PaymentStatus status,
                      List<UserMinInfo> users) {
}
