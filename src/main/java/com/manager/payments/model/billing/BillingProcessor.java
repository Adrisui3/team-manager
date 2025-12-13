package com.manager.payments.model.billing;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptFactory;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;

public class BillingProcessor {

    private BillingProcessor() {
    }

    public static Optional<Receipt> process(PlayerPaymentAssignment playerPaymentAssignment, LocalDate date,
                                            Predicate<Receipt> receiptExists) {
        Payment payment = playerPaymentAssignment.payment();
        if (date.isAfter(payment.endDate()))
            return Optional.empty();

        Receipt receipt = switch (payment.periodicity()) {
            case MONTHLY, QUARTERLY -> {
                BillingPeriod currentBillingPeriod = BillingPeriodFactory.build(payment, date);
                yield ReceiptFactory.buildForBillingPeriod(playerPaymentAssignment, currentBillingPeriod, date);
            }
            case ONCE -> ReceiptFactory.buildForUniquePayment(playerPaymentAssignment, date);
        };

        if (!receiptExists.test(receipt)) {
            return Optional.of(receipt);
        }

        return Optional.empty();
    }
}
