package com.manager.payments.model.billing;

import com.manager.payments.model.payments.Payment;

import java.time.LocalDate;
import java.time.YearMonth;

public class BillingPeriodFactory {

    private BillingPeriodFactory() {
    }

    public static BillingPeriod build(Payment payment, LocalDate currentDate) {
        LocalDate start = getPeriodStart(payment, currentDate);
        LocalDate end = getPeriodEnd(payment, currentDate, start);
        return new BillingPeriod(start, end.isAfter(payment.endDate()) ? payment.endDate() : end);
    }

    private static LocalDate getPeriodStart(Payment payment, LocalDate date) {
        return switch (payment.periodicity()) {
            case MONTHLY -> YearMonth.from(date).atDay(1);
            case QUARTERLY -> {
                LocalDate quarterStart = YearMonth.from(payment.startDate()).atDay(1);
                while (!quarterStart.isAfter(date)) {
                    quarterStart = quarterStart.plusMonths(3);
                }

                yield quarterStart.minusMonths(3);
            }
            case ONCE -> date;
        };
    }

    private static LocalDate getPeriodEnd(Payment payment, LocalDate date, LocalDate start) {
        return switch (payment.periodicity()) {
            case MONTHLY -> YearMonth.from(date).atEndOfMonth();
            case QUARTERLY -> start.plusMonths(3).minusDays(1);
            case ONCE -> date;
        };
    }
}

