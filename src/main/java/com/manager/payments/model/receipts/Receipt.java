package com.manager.payments.model.receipts;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
public record Receipt(UUID id, String code, BigDecimal amount, LocalDate issuedDate, LocalDate paymentDate,
                      LocalDate expiryDate, LocalDate periodStartDate, LocalDate periodEndDate, ReceiptStatus status,
                      Player player, Payment payment) {

    private static final int RECEIPT_EXPIRATION_DAYS = 15;

    public Receipt(String code, BigDecimal amount, LocalDate issuedDate, LocalDate periodStartDate,
                   LocalDate periodEndDate, ReceiptStatus status, Player player, Payment payment) {
        this(null, code, amount.setScale(2, RoundingMode.HALF_UP), issuedDate, null,
                issuedDate.plusDays(RECEIPT_EXPIRATION_DAYS), periodStartDate, periodEndDate, status, player, payment);
    }

    public Receipt withStatus(ReceiptStatus status) {
        return new Receipt(id, code, amount, issuedDate, paymentDate, expiryDate, periodStartDate, periodEndDate,
                status, player, payment);
    }
}
