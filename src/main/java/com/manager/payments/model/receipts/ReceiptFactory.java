package com.manager.payments.model.receipts;

import com.manager.payments.model.payments.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReceiptFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptFactory.class);
    private static final int RECEIPT_EXPIRATION_DAYS = 15;

    private ReceiptFactory() {
    }

    public static ReceiptMinInfo buildMinInfo(Payment payment) {
        LocalDate issuedDate = LocalDate.now();
        LocalDate nextPaymentDate = payment.nextPaymentDate();
        LocalDate expiryDate = issuedDate.plusDays(RECEIPT_EXPIRATION_DAYS);

        long daysUntilNext = ChronoUnit.DAYS.between(issuedDate, nextPaymentDate);
        if (daysUntilNext < 0) {
            LOGGER.info("Next payment date {} is before issued date {}. Full receipt will be generated.",
                    nextPaymentDate, issuedDate);
        }
        double remainderPercentage = (double) daysUntilNext / payment.periodDays();
        if (remainderPercentage <= 0d || remainderPercentage > 1d) {
            remainderPercentage = 1d;
        }

        double amount = payment.amount() * remainderPercentage;
        return new ReceiptMinInfo(null, amount, issuedDate, null, expiryDate, ReceiptStatus.PENDING);
    }
}
