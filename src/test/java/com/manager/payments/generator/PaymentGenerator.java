package com.manager.payments.generator;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class PaymentGenerator {

    private final Payment.PaymentBuilder builder;

    private PaymentGenerator() {
        this.builder = Payment.builder();
    }

    public static PaymentGenerator payment() {
        PaymentGenerator generator = new PaymentGenerator();
        generator.id(UUID.randomUUID());
        generator.code("PAY-001");
        generator.amount(BigDecimal.valueOf(50));
        generator.name("Monthly Fee");
        generator.description("Monthly membership fee");
        generator.startDate(LocalDate.of(2025, 9, 1));
        generator.endDate(LocalDate.of(2025, 9, 30));
        generator.periodicity(Periodicity.MONTHLY);
        generator.status(PaymentStatus.ACTIVE);
        generator.createdAt(LocalDateTime.now());
        generator.updatedAt(LocalDateTime.now());
        return generator;
    }

    public PaymentGenerator id(UUID id) {
        builder.id(id);
        return this;
    }

    public PaymentGenerator code(String code) {
        builder.code(code);
        return this;
    }

    public PaymentGenerator amount(BigDecimal amount) {
        builder.amount(amount);
        return this;
    }

    public PaymentGenerator name(String name) {
        builder.name(name);
        return this;
    }

    public PaymentGenerator description(String description) {
        builder.description(description);
        return this;
    }

    public PaymentGenerator startDate(LocalDate startDate) {
        builder.startDate(startDate);
        return this;
    }

    public PaymentGenerator endDate(LocalDate endDate) {
        builder.endDate(endDate);
        return this;
    }

    public PaymentGenerator periodicity(Periodicity periodicity) {
        builder.periodicity(periodicity);
        return this;
    }

    public PaymentGenerator status(PaymentStatus status) {
        builder.status(status);
        return this;
    }

    public PaymentGenerator updatedAt(LocalDateTime updatedAt) {
        builder.updatedAt(updatedAt);
        return this;
    }

    public PaymentGenerator createdAt(LocalDateTime createdAt) {
        builder.createdAt(createdAt);
        return this;
    }

    public Payment build() {
        return builder.build();
    }
}
