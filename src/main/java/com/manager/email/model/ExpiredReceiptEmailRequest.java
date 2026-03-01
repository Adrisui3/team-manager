package com.manager.email.model;

import com.manager.payments.model.receipts.Receipt;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ExpiredReceiptEmailRequest(
        String playerName,
        String playerEmail,
        String playerPersonalId,
        String paymentName,
        LocalDate expiryDate,
        LocalDate periodStartDate,
        LocalDate periodEndDate,
        BigDecimal amount
) {

    public static ExpiredReceiptEmailRequest buildFromReceipt(Receipt receipt) {
        return ExpiredReceiptEmailRequest.builder()
                .playerName(receipt.player().name())
                .playerEmail(receipt.player().email())
                .playerPersonalId(receipt.player().personalId())
                .paymentName(receipt.payment().name())
                .expiryDate(receipt.expiryDate())
                .periodStartDate(receipt.periodStartDate())
                .periodEndDate(receipt.periodEndDate())
                .amount(receipt.amount())
                .build();
    }

}
