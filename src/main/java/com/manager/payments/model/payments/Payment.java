package com.manager.payments.model.payments;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
public record Payment(UUID id, String code, BigDecimal amount, String name, String description, LocalDate startDate,
                      LocalDate endDate, Periodicity periodicity, PaymentStatus status) {

}
