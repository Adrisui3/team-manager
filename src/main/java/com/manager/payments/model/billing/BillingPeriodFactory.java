package com.manager.payments.model.billing;

import com.manager.payments.model.payments.Periodicity;

import java.time.LocalDate;
import java.time.YearMonth;

public class BillingPeriodFactory {

    private BillingPeriodFactory() {
    }

    public static BillingPeriod build(Periodicity periodicity, LocalDate date) {
        LocalDate start = getPeriodStart(periodicity, date);
        LocalDate end = getPeriodEnd(periodicity, date);
        return new BillingPeriod(start, end);
    }

    private static LocalDate getPeriodStart(Periodicity periodicity, LocalDate date) {
        return switch (periodicity) {
            case MONTHLY -> YearMonth.from(date).atDay(1);
            case QUARTERLY -> {
                int currentQuarter = ((date.getMonthValue() - 1) / 3) + 1;
                int startMonth = (currentQuarter - 1) * 3 + 1;
                yield LocalDate.of(date.getYear(), startMonth, 1);
            }
            case ONCE -> date;
        };
    }

    private static LocalDate getPeriodEnd(Periodicity periodicity, LocalDate date) {
        return switch (periodicity) {
            case MONTHLY -> YearMonth.from(date).atEndOfMonth();
            case QUARTERLY -> getPeriodStart(periodicity, date).plusMonths(3).minusDays(1);
            case ONCE -> date;
        };
    }
}

