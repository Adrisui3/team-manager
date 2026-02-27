package com.manager.email.generator;

import com.manager.email.model.ExpiredReceiptEmailRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class ExpiredReceiptEmailRequestGenerator {

    private final ExpiredReceiptEmailRequest.ExpiredReceiptEmailRequestBuilder builder;

    private ExpiredReceiptEmailRequestGenerator() {
        this.builder = ExpiredReceiptEmailRequest.builder();
    }

    public static ExpiredReceiptEmailRequestGenerator periodicRequest() {
        ExpiredReceiptEmailRequestGenerator generator = new ExpiredReceiptEmailRequestGenerator();
        generator.playerName("John Doe");
        generator.playerEmail("player@test.com");
        generator.playerPersonalId("12345678");
        generator.paymentName("Monthly Fee");
        generator.expiryDate(LocalDate.of(2026, 3, 1));
        generator.periodStartDate(LocalDate.of(2026, 1, 1));
        generator.periodEndDate(LocalDate.of(2026, 1, 31));
        generator.amount(new BigDecimal("150.00"));
        return generator;
    }

    public static ExpiredReceiptEmailRequestGenerator uniqueRequest() {
        ExpiredReceiptEmailRequestGenerator generator = new ExpiredReceiptEmailRequestGenerator();
        generator.playerName("John Doe");
        generator.playerEmail("player@test.com");
        generator.playerPersonalId("12345678");
        generator.paymentName("Registration Fee");
        generator.expiryDate(LocalDate.of(2026, 3, 1));
        generator.amount(new BigDecimal("200.00"));
        return generator;
    }

    public ExpiredReceiptEmailRequestGenerator playerName(String playerName) {
        builder.playerName(playerName);
        return this;
    }

    public ExpiredReceiptEmailRequestGenerator playerEmail(String playerEmail) {
        builder.playerEmail(playerEmail);
        return this;
    }

    public ExpiredReceiptEmailRequestGenerator playerPersonalId(String playerPersonalId) {
        builder.playerPersonalId(playerPersonalId);
        return this;
    }

    public ExpiredReceiptEmailRequestGenerator paymentName(String paymentName) {
        builder.paymentName(paymentName);
        return this;
    }

    public ExpiredReceiptEmailRequestGenerator expiryDate(LocalDate expiryDate) {
        builder.expiryDate(expiryDate);
        return this;
    }

    public ExpiredReceiptEmailRequestGenerator periodStartDate(LocalDate periodStartDate) {
        builder.periodStartDate(periodStartDate);
        return this;
    }

    public ExpiredReceiptEmailRequestGenerator periodEndDate(LocalDate periodEndDate) {
        builder.periodEndDate(periodEndDate);
        return this;
    }

    public ExpiredReceiptEmailRequestGenerator amount(BigDecimal amount) {
        builder.amount(amount);
        return this;
    }

    public ExpiredReceiptEmailRequest build() {
        return builder.build();
    }
}
