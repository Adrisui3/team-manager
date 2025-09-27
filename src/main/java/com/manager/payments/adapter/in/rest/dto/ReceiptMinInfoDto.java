package com.manager.payments.adapter.in.rest.dto;

import com.manager.payments.model.receipts.ReceiptStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ReceiptMinInfoDto(UUID id, double amount, LocalDate issuedDate, LocalDate paymentDate,
                                LocalDate expiryDate, ReceiptStatus status) {
}
