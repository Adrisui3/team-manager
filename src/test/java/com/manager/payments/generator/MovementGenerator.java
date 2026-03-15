package com.manager.payments.generator;

import com.manager.payments.model.movements.Movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public final class MovementGenerator {

    private final Movement.MovementBuilder builder;

    private MovementGenerator() {
        this.builder = Movement.builder();
    }

    public static MovementGenerator movement() {
        MovementGenerator generator = new MovementGenerator();
        generator.date(LocalDate.of(2026, 3, 1));
        generator.valueDate(LocalDate.of(2026, 3, 5));
        generator.movementName("Transferencia");
        generator.concept("12345678-PAY-001-092025");
        generator.amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP));
        return generator;
    }

    public MovementGenerator date(LocalDate date) {
        builder.date(date);
        return this;
    }

    public MovementGenerator valueDate(LocalDate valueDate) {
        builder.valueDate(valueDate);
        return this;
    }

    public MovementGenerator movementName(String movementName) {
        builder.movementName(movementName);
        return this;
    }

    public MovementGenerator concept(String concept) {
        builder.concept(concept);
        return this;
    }

    public MovementGenerator amount(BigDecimal amount) {
        builder.amount(amount);
        return this;
    }

    public Movement build() {
        return builder.build();
    }
}
