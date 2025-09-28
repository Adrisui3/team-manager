package com.manager.payments.adapter.in.rest.dto.models;

import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;

import java.time.LocalDate;
import java.util.UUID;

public record PaymentDto(UUID id, String code, double amount, String name, String description, LocalDate startDate,
                         LocalDate endDate, Periodicity periodicity, PaymentStatus status) {
}
