package com.manager.payments.model.billing;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BillingProcessor {

    private BillingProcessor() {
    }

    public static List<Receipt> process(PlayerPaymentAssignment playerPaymentAssignment, LocalDate date,
                                        Function<Receipt, Boolean> receiptExists) {
        Payment payment = playerPaymentAssignment.payment();
        LocalDate cursor = BillingPeriodFactory.getPeriodStart(payment.periodicity(), payment.startDate());
        List<Receipt> receiptCandidates = new ArrayList<>();
        while (!cursor.isAfter(date)) {
            BillingPeriod billingPeriod = BillingPeriodFactory.build(payment.periodicity(), cursor);
            if (billingPeriod.start().isAfter(payment.endDate()))
                break;

            Receipt receipt = ReceiptFactory.build(playerPaymentAssignment, billingPeriod, date);
            if (!receiptExists.apply(receipt)) {
                receiptCandidates.add(receipt);
            }

            cursor = BillingPeriodFactory.nextPeriodStart(payment.periodicity(), cursor);
        }

        return receiptCandidates;
    }
}
