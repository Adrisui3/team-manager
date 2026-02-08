package com.manager.payments.model.receipts;

import com.manager.payments.model.exceptions.InvalidReceiptAmountException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Receipt(UUID id, String code, BigDecimal amount, LocalDate issuedDate, LocalDate paymentDate,
                      LocalDate expiryDate, LocalDate periodStartDate, LocalDate periodEndDate, ReceiptStatus status,
                      Player player, Payment payment, LocalDateTime updatedAt, LocalDateTime createdAt) {

    public Receipt update(BigDecimal newAmount, LocalDate newExpiryDate, ReceiptStatus newStatus) {
        LocalDate paymentDate;
        if (!newStatus.equals(ReceiptStatus.PAID)) {
            paymentDate = null;
        } else {
            paymentDate = status().equals(ReceiptStatus.PAID) ? paymentDate() : LocalDate.now();
        }

        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidReceiptAmountException();
        }

        return toBuilder()
                .amount(newAmount)
                .status(newStatus)
                .expiryDate(newExpiryDate)
                .paymentDate(paymentDate)
                .build();
    }
}
