package com.manager.payments.model.receipts;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
public record Receipt(UUID id, String code, BigDecimal amount, LocalDate issuedDate, LocalDate paymentDate,
                      LocalDate expiryDate, LocalDate periodStartDate, LocalDate periodEndDate, ReceiptStatus status,
                      Player player, Payment payment) {
}
