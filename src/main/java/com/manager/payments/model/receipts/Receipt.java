package com.manager.payments.model.receipts;

import java.time.LocalDate;
import java.util.UUID;

public record Receipt(UUID id, double amount, LocalDate issuedDate, LocalDate paymentDate, LocalDate expiryDate,
                      int paymentPeriodInDays, ReceiptStatus status) {
}
