package com.manager.payments.model.receipts;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.billing.BillingPeriod;
import com.manager.payments.model.billing.BillingPeriodFactory;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReceiptFactoryTest {

    @Test
    void shouldCreateCompleteReceiptWhenDateIsStartPaymentDate() {
        // given
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 30);
        Payment payment = new Payment("PAYMENT", 50, "", "", startDate, endDate,
                Periodicity.MONTHLY, PaymentStatus.ACTIVE);
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment.periodicity(), startDate);
        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(null, payment, true);

        //then
        Receipt receipt = ReceiptFactory.build(playerPaymentAssignment, billingPeriod, startDate);

        //then
        assertThat(receipt.amount()).isEqualTo(payment.amount());
    }

    @Test
    void shouldCreateHalfReceiptWhenDateIsInTheMiddleOfTheMonth() {
        // given
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 30);
        LocalDate currentDate = LocalDate.of(2025, 9, 16);
        Payment payment = new Payment("PAYMENT", 50, "", "", startDate, endDate,
                Periodicity.MONTHLY, PaymentStatus.ACTIVE);
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment.periodicity(), startDate);
        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(null, payment, true);

        //then
        Receipt receipt = ReceiptFactory.build(playerPaymentAssignment, billingPeriod, currentDate);

        //then
        assertThat(receipt.amount()).isEqualTo(payment.amount() / 2);
    }

    @Test
    void shouldCreateCompleteReceiptIfBillingPeriodIfBillingIsInThePast() {
        // given
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 30);
        LocalDate currentDate = LocalDate.of(2025, 10, 16);
        Payment payment = new Payment("PAYMENT", 50, "", "", startDate, endDate,
                Periodicity.MONTHLY, PaymentStatus.ACTIVE);
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment.periodicity(), startDate);
        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(null, payment, true);

        //then
        Receipt receipt = ReceiptFactory.build(playerPaymentAssignment, billingPeriod, currentDate);

        //then
        assertThat(receipt.amount()).isEqualTo(payment.amount());
    }

}
