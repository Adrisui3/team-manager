package com.manager.payments.generator;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ReceiptGenerator {

    private final Receipt.ReceiptBuilder builder;

    private ReceiptGenerator() {
        this.builder = Receipt.builder();
    }

    public static ReceiptGenerator receipt() {
        ReceiptGenerator generator = new ReceiptGenerator();
        generator.code("REC-001");
        generator.amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP));
        generator.issuedDate(LocalDate.now());
        generator.expiryDate(LocalDate.now().plusDays(15));
        generator.status(ReceiptStatus.PENDING);
        return generator;
    }

    public ReceiptGenerator id(UUID id) {
        builder.id(id);
        return this;
    }

    public ReceiptGenerator code(String code) {
        builder.code(code);
        return this;
    }

    public ReceiptGenerator amount(BigDecimal amount) {
        builder.amount(amount);
        return this;
    }

    public ReceiptGenerator issuedDate(LocalDate issuedDate) {
        builder.issuedDate(issuedDate);
        return this;
    }

    public ReceiptGenerator paymentDate(LocalDate paymentDate) {
        builder.paymentDate(paymentDate);
        return this;
    }

    public ReceiptGenerator expiryDate(LocalDate expiryDate) {
        builder.expiryDate(expiryDate);
        return this;
    }

    public ReceiptGenerator periodStartDate(LocalDate periodStartDate) {
        builder.periodStartDate(periodStartDate);
        return this;
    }

    public ReceiptGenerator periodEndDate(LocalDate periodEndDate) {
        builder.periodEndDate(periodEndDate);
        return this;
    }

    public ReceiptGenerator status(ReceiptStatus status) {
        builder.status(status);
        return this;
    }

    public ReceiptGenerator player(Player player) {
        builder.player(player);
        return this;
    }

    public ReceiptGenerator payment(Payment payment) {
        builder.payment(payment);
        return this;
    }

    public ReceiptGenerator updatedAt(LocalDateTime updatedAt) {
        builder.updatedAt(updatedAt);
        return this;
    }

    public ReceiptGenerator createdAt(LocalDateTime createdAt) {
        builder.createdAt(createdAt);
        return this;
    }

    public Receipt build() {
        return builder.build();
    }
}
