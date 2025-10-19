package com.manager.payments.model.billing;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptFactory;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public class BillingProcessor {

    private BillingProcessor() {
    }

    public static Optional<Receipt> process(PlayerPaymentAssignment playerPaymentAssignment, LocalDate date,
                                            Function<Receipt, Boolean> receiptExists) {
        Payment payment = playerPaymentAssignment.payment();
        if (date.isAfter(payment.endDate()))
            return Optional.empty();

        BillingPeriod currentBillingPeriod = BillingPeriodFactory.build(payment, date);
        Receipt receipt = ReceiptFactory.build(playerPaymentAssignment, currentBillingPeriod, date);
        if (!receiptExists.apply(receipt)) {
            return Optional.of(receipt);
        }

        return Optional.empty();
    }
}
