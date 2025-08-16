package com.manager.payments.model.receipts;

import com.manager.payments.model.users.UserMinInfo;

import java.time.LocalDate;
import java.util.UUID;

public record Receipt(UUID id, double amount, LocalDate issuedDate, LocalDate paymentDate, LocalDate expiryDate,
                      ReceiptStatus status, UserMinInfo user) {
}
