package com.manager.payments.model.receipts;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.billing.BillingPeriod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReceiptFactory {

    private ReceiptFactory() {
    }

    public static Receipt build(PlayerPaymentAssignment playerPaymentAssignment, BillingPeriod billingPeriod,
                                LocalDate date) {
        long daysUntilNext = ChronoUnit.DAYS.between(date, billingPeriod.end()) + 1;
        long daysInPeriod = ChronoUnit.DAYS.between(billingPeriod.start(), billingPeriod.end()) + 1;

        double remainderPercentage = (double) daysUntilNext / daysInPeriod;
        if (remainderPercentage <= 0d || remainderPercentage > 1d) {
            remainderPercentage = 1d;
        }

        BigDecimal amount =
                playerPaymentAssignment.payment().amount().multiply(BigDecimal.valueOf(remainderPercentage));
        String code = buildReceiptCode(playerPaymentAssignment, billingPeriod);

        return new Receipt(code, amount, date, billingPeriod.start(), billingPeriod.end(), ReceiptStatus.PENDING,
                playerPaymentAssignment.player(), playerPaymentAssignment.payment());
    }

    private static String buildReceiptCode(PlayerPaymentAssignment playerPaymentAssignment,
                                           BillingPeriod billingPeriod) {
        String playerId = playerPaymentAssignment.player().personalId();
        String paymentCode = playerPaymentAssignment.payment().code();
        String code = String.join("-", playerId, paymentCode);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMuuuu");
        String dates = switch (playerPaymentAssignment.payment().periodicity()) {
            case MONTHLY -> billingPeriod.start().format(fmt);
            case QUARTERLY -> String.join("-", billingPeriod.start().format(fmt), billingPeriod.end().format(fmt));
            case ONCE -> "";
        };

        return String.join("-", code, dates);
    }
}
