package com.manager.payments.model.receipts;

import com.manager.payments.model.users.PlayerMinInfo;

import java.time.LocalDate;
import java.util.UUID;

public record Receipt(UUID id, double amount, LocalDate issuedDate, LocalDate paymentDate, LocalDate expiryDate,
                      ReceiptStatus status, PlayerMinInfo user) {

    public Receipt withStatus(ReceiptStatus status) {
        return new Receipt(id, amount, issuedDate, paymentDate, expiryDate, status, user);
    }
}
