package com.manager.payments.model.payments;

import java.util.List;

public class ExpiredPaymentProcessor {

    private ExpiredPaymentProcessor() {
    }

    public static List<Payment> processExpiredPayments(List<Payment> expiredPayments) {
        return expiredPayments.stream().map(payment -> payment.withStatus(PaymentStatus.EXPIRED)).toList();
    }
}
