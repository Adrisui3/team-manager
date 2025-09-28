package com.manager.payments.model.receipts;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.billing.BillingPeriod;
import com.manager.payments.model.billing.BillingPeriodFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReceiptFactory {

    private ReceiptFactory() {
    }

    public static Receipt build(PlayerPaymentAssignment playerPaymentAssignment) {
        BillingPeriod billingPeriod = BillingPeriodFactory.build(playerPaymentAssignment.payment().periodicity(),
                LocalDate.now());
        long daysUntilNext = ChronoUnit.DAYS.between(LocalDate.now(), billingPeriod.end()) + 1;
        long daysInPeriod = ChronoUnit.DAYS.between(billingPeriod.start(), billingPeriod.end()) + 1;

        double remainderPercentage = (double) daysUntilNext / daysInPeriod;
        if (remainderPercentage <= 0d || remainderPercentage > 1d) {
            remainderPercentage = 1d;
        }

        double amount = playerPaymentAssignment.payment().amount() * remainderPercentage;
        return new Receipt(amount, billingPeriod.start(), billingPeriod.end(), ReceiptStatus.PENDING,
                playerPaymentAssignment);
    }
}
