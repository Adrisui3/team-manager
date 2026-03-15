package com.manager.payments.adapter.in.rest.dto.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovementDto(
        LocalDate date,
        LocalDate valueDate,
        String movementName,
        String concept,
        BigDecimal amount
) {
}
