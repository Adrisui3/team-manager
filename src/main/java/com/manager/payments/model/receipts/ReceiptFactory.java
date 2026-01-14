package com.manager.payments.model.receipts;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.billing.BillingPeriod;
import com.manager.payments.model.payments.Payment;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReceiptFactory {

    private static final int RECEIPT_EXPIRATION_DAYS = 15;

    private ReceiptFactory() {
    }

    public static Receipt buildForUniquePayment(PlayerPaymentAssignment playerPaymentAssignment, LocalDate date) {
        String code = buildReceiptCode(playerPaymentAssignment, null);
        return Receipt.builder()
                .code(code)
                .amount(playerPaymentAssignment.payment().amount().setScale(2, RoundingMode.HALF_UP))
                .issuedDate(date)
                .expiryDate(date.plusDays(RECEIPT_EXPIRATION_DAYS))
                .status(ReceiptStatus.PENDING)
                .player(playerPaymentAssignment.player())
                .payment(playerPaymentAssignment.payment())
                .build();
    }

    public static Receipt buildForBillingPeriod(PlayerPaymentAssignment playerPaymentAssignment,
                                                BillingPeriod billingPeriod, LocalDate date) {
        BigDecimal amount = computeReceiptAmount(playerPaymentAssignment.payment(), billingPeriod, date);
        String code = buildReceiptCode(playerPaymentAssignment, billingPeriod);
        return Receipt.builder()
                .code(code)
                .amount(amount.setScale(2, RoundingMode.HALF_UP))
                .issuedDate(date)
                .expiryDate(date.plusDays(RECEIPT_EXPIRATION_DAYS))
                .periodStartDate(billingPeriod.start())
                .periodEndDate(billingPeriod.end())
                .status(ReceiptStatus.PENDING)
                .player(playerPaymentAssignment.player())
                .payment(playerPaymentAssignment.payment())
                .build();
    }

    private static BigDecimal computeReceiptAmount(Payment payment, BillingPeriod billingPeriod, LocalDate date) {
        LocalDate billingEnd = payment.endDate().isBefore(billingPeriod.end()) ? payment.endDate() :
                billingPeriod.end();

        long daysUntilNext = ChronoUnit.DAYS.between(date, billingEnd) + 1;
        long daysInPeriod = ChronoUnit.DAYS.between(billingPeriod.start(), billingPeriod.end()) + 1;
        double remainderPercentage = (double) daysUntilNext / daysInPeriod;
        return payment.amount().multiply(BigDecimal.valueOf(remainderPercentage));
    }

    private static String buildReceiptCode(PlayerPaymentAssignment playerPaymentAssignment,
                                           @Nullable BillingPeriod billingPeriod) {
        String playerId = playerPaymentAssignment.player().personalId();
        String paymentCode = playerPaymentAssignment.payment().code();
        String code = String.join("-", playerId, paymentCode);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMuuuu");
        String dates = switch (playerPaymentAssignment.payment().periodicity()) {
            case MONTHLY -> billingPeriod.start().format(fmt);
            case QUARTERLY -> String.join("-", billingPeriod.start().format(fmt), billingPeriod.end().format(fmt));
            case ONCE -> "";
        };

        return dates.isEmpty() ? code : String.join("-", code, dates);
    }
}
