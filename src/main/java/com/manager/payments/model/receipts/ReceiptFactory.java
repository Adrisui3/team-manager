package com.manager.payments.model.receipts;

import com.manager.payments.model.payments.Payment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReceiptFactory {

    private static final int RECEIPT_EXPIRATION_DAYS = 15;

    private ReceiptFactory() {
    }

    public static ReceiptMinInfo buildMinInfo(Payment payment) {
        LocalDate issuedDate = LocalDate.now();
        LocalDate expiryDate = issuedDate.plusDays(RECEIPT_EXPIRATION_DAYS);

        double amount =
                payment.amount() * ((double) (ChronoUnit.DAYS.between(issuedDate, payment.nextPaymentDate())) / payment.periodDays());

        return new ReceiptMinInfo(null, amount, issuedDate, null, expiryDate, ReceiptStatus.PENDING);
    }
}
