package com.manager.payments.model.receipts;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.billing.BillingPeriod;
import com.manager.payments.model.billing.BillingPeriodFactory;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReceiptFactoryTest {

    @Test
    void shouldCreateCompleteReceiptWhenDateIsStartPaymentDate() {
        // given
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 30);
        Player player = new Player("123456789A", "", "", "", null, Category.NONE, PlayerStatus.ENABLED);
        Payment payment = new Payment("PAYMENT", BigDecimal.valueOf(50), "", "", startDate, endDate,
                Periodicity.MONTHLY, PaymentStatus.ACTIVE);
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment, startDate);
        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(player, payment);

        //then
        Receipt receipt = ReceiptFactory.buildForBillingPeriod(playerPaymentAssignment, billingPeriod, startDate);

        //then
        assertThat(receipt.amount()).isEqualTo(payment.amount());
        assertThat(receipt.code()).isEqualTo("123456789A-PAYMENT-092025");
    }

    @Test
    void shouldCreateHalfReceiptWhenDateIsInTheMiddleOfTheMonth() {
        // given
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 30);
        LocalDate currentDate = LocalDate.of(2025, 9, 16);
        Player player = new Player("123456789A", "", "", "", null, Category.NONE, PlayerStatus.ENABLED);
        Payment payment = new Payment("PAYMENT", BigDecimal.valueOf(50), "", "", startDate, endDate,
                Periodicity.MONTHLY, PaymentStatus.ACTIVE);
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment, startDate);
        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(player, payment);

        //then
        Receipt receipt = ReceiptFactory.buildForBillingPeriod(playerPaymentAssignment, billingPeriod, currentDate);

        //then
        assertThat(receipt.amount()).isEqualTo(payment.amount().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
    }

    @Test
    void shouldCreateCompleteReceiptIfBillingPeriodIfBillingIsInThePast() {
        // given
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 30);
        LocalDate currentDate = LocalDate.of(2025, 10, 16);
        Player player = new Player("123456789A", "", "", "", null, Category.NONE, PlayerStatus.ENABLED);
        Payment payment = new Payment("PAYMENT", BigDecimal.valueOf(50), "", "", startDate, endDate,
                Periodicity.MONTHLY, PaymentStatus.ACTIVE);
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment, startDate);
        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(player, payment);

        //then
        Receipt receipt = ReceiptFactory.buildForBillingPeriod(playerPaymentAssignment, billingPeriod, currentDate);

        //then
        assertThat(receipt.amount()).isEqualTo(payment.amount());
    }

    @Test
    void shouldCreateReceiptForUniquePayment() {
        // given
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 30);
        LocalDate currentDate = LocalDate.of(2025, 10, 16);
        Player player = new Player("123456789A", "", "", "", null, Category.NONE, PlayerStatus.ENABLED);
        Payment payment = new Payment("PAYMENT", BigDecimal.valueOf(50), "", "", startDate, endDate,
                Periodicity.ONCE, PaymentStatus.ACTIVE);
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment, startDate);
        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(player, payment);

        //then
        Receipt receipt = ReceiptFactory.buildForBillingPeriod(playerPaymentAssignment, billingPeriod, currentDate);

        //then
        assertThat(receipt.amount()).isEqualTo(payment.amount());
        assertThat(receipt.code()).isEqualTo("123456789A-PAYMENT");
    }
}
