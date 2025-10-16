package com.manager.payments.model.receipts;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;

import java.time.LocalDate;
import java.util.UUID;

public record Receipt(UUID id, String code, double amount, LocalDate issuedDate, LocalDate paymentDate,
                      LocalDate expiryDate, LocalDate periodStartDate, LocalDate periodEndDate, ReceiptStatus status,
                      Player player, Payment payment) {

    private static final int RECEIPT_EXPIRATION_DAYS = 15;

    public Receipt(String code, double amount, LocalDate issuedDate, LocalDate periodStartDate, LocalDate periodEndDate,
                   ReceiptStatus status, Player player, Payment payment) {
        this(null, code, amount, issuedDate, null, issuedDate.plusDays(RECEIPT_EXPIRATION_DAYS), periodStartDate,
                periodEndDate, status, player, payment);
    }

    public Receipt withStatus(ReceiptStatus status) {
        return new Receipt(id, code, amount, issuedDate, paymentDate, expiryDate, periodStartDate, periodEndDate,
                status, player, payment);
    }
}
