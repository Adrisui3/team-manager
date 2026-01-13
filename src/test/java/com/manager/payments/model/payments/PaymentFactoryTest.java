package com.manager.payments.model.payments;

import com.manager.payments.model.exceptions.PeriodicPaymentWithoutIntervalException;
import com.manager.payments.model.exceptions.UniquePaymentWithIntervalException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PaymentFactoryTest {

    @Test
    void shouldMarkPaymentActiveWhenStartDateIsToday() {
        //given
        LocalDate today = LocalDate.now();

        //when
        Payment payment = PaymentFactory.build("CODE", BigDecimal.valueOf(50), "name", "desc", today,
                today.plusDays(30),
                Periodicity.MONTHLY);

        //when
        assertThat(payment.status()).isEqualTo(PaymentStatus.ACTIVE);
    }

    @Test
    void shouldMarkPaymentInactiveWhenStartDateIsTomorrow() {
        //given
        LocalDate today = LocalDate.now();

        //when
        Payment payment = PaymentFactory.build("CODE", BigDecimal.valueOf(50), "name", "desc", today.plusDays(1),
                today.plusDays(30),
                Periodicity.MONTHLY);

        //then
        assertThat(payment.status()).isEqualTo(PaymentStatus.INACTIVE);
    }

    @Test
    void shouldThrowExceptionWhenPeriodicPaymentHasNoInterval() {
        //given - when - then
        assertThatThrownBy(() -> PaymentFactory.build("CODE", BigDecimal.valueOf(50), "name", "desc", null, null,
                Periodicity.MONTHLY)).isInstanceOf(PeriodicPaymentWithoutIntervalException.class);
    }

    @Test
    void shouldThrowExceptionWhenUniquePaymentHasInterval() {
        //given
        LocalDate today = LocalDate.now();

        //when - then
        assertThatThrownBy(() -> PaymentFactory.build("CODE", BigDecimal.valueOf(50), "name", "desc",
                today.plusDays(1), today.plusDays(30), Periodicity.ONCE)).isInstanceOf(UniquePaymentWithIntervalException.class);
    }
}
