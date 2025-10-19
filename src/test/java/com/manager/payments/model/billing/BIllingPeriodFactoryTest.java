package com.manager.payments.model.billing;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.Periodicity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BillingPeriodFactoryTest {

    @Test
    void shouldBuildMonthlyBillingPeriod() {
        // given
        LocalDate currentDate = LocalDate.of(2025, 6, 15);
        Payment payment = Mockito.mock(Payment.class);
        Mockito.when(payment.periodicity()).thenReturn(Periodicity.MONTHLY);
        Mockito.when(payment.endDate()).thenReturn(LocalDate.of(2026, 6, 30));

        //  when
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment, currentDate);

        // then
        assertThat(billingPeriod.start()).isEqualTo(YearMonth.from(currentDate).atDay(1));
        assertThat(billingPeriod.end()).isEqualTo(YearMonth.from(currentDate).atEndOfMonth());
    }

    @Test
    void shouldBuildQuarterlyFirstBillingPeriod() {
        // given
        LocalDate currentDate = LocalDate.of(2025, 6, 15);
        Payment payment = Mockito.mock(Payment.class);
        Mockito.when(payment.periodicity()).thenReturn(Periodicity.QUARTERLY);
        Mockito.when(payment.startDate()).thenReturn(YearMonth.from(currentDate).atDay(1));
        Mockito.when(payment.endDate()).thenReturn(LocalDate.of(2026, 6, 30));

        //  when
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment, currentDate);

        // then
        assertThat(billingPeriod.start()).isEqualTo(YearMonth.from(payment.startDate()).atDay(1));
        assertThat(billingPeriod.end()).isEqualTo(LocalDate.of(2025, 8, 31));
    }

    @Test
    void shouldBuildQuarterlyFirstBillingPeriodFromFirstDay() {
        // given
        LocalDate currentDate = LocalDate.of(2025, 6, 1);
        Payment payment = Mockito.mock(Payment.class);
        Mockito.when(payment.periodicity()).thenReturn(Periodicity.QUARTERLY);
        Mockito.when(payment.startDate()).thenReturn(currentDate);
        Mockito.when(payment.endDate()).thenReturn(LocalDate.of(2026, 6, 30));

        //  when
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment, currentDate);

        // then
        assertThat(billingPeriod.start()).isEqualTo(currentDate);
        assertThat(billingPeriod.end()).isEqualTo(LocalDate.of(2025, 8, 31));
    }

    @Test
    void shouldBuildQuarterlySecondBillingPeriod() {
        // given
        LocalDate currentDate = LocalDate.of(2025, 12, 15);
        Payment payment = Mockito.mock(Payment.class);
        Mockito.when(payment.periodicity()).thenReturn(Periodicity.QUARTERLY);
        Mockito.when(payment.startDate()).thenReturn(YearMonth.from(currentDate).atDay(1));
        Mockito.when(payment.endDate()).thenReturn(LocalDate.of(2026, 6, 30));

        //  when
        BillingPeriod billingPeriod = BillingPeriodFactory.build(payment, currentDate);

        // then
        assertThat(billingPeriod.start()).isEqualTo(LocalDate.of(2025, 12, 1));
        assertThat(billingPeriod.end()).isEqualTo(LocalDate.of(2026, 2, 28));
    }

}
