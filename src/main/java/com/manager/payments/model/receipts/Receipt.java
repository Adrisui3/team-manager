package com.manager.payments.model.receipts;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;

import java.time.LocalDate;
import java.util.UUID;

public record Receipt(UUID id, double amount, LocalDate issuedDate, LocalDate paymentDate, LocalDate expiryDate,
                      LocalDate periodStartDate, LocalDate periodEndDate,
                      ReceiptStatus status, PlayerPaymentAssignment playerPaymentAssignment) {

    public Receipt withStatus(ReceiptStatus status) {
        return new Receipt(id, amount, issuedDate, paymentDate, expiryDate, periodStartDate, periodEndDate, status,
                playerPaymentAssignment);
    }
}
