package com.manager.payments.adapter.in.rest.dto;

import com.manager.payments.model.payments.PaymentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PaymentDto(UUID id, double amount, String name, String description, LocalDate startDate,
                         LocalDate nextPaymentDate, LocalDate endDate, int periodDays, PaymentStatus status,
                         List<PlayerMInInfoDto> players) {
}
