package com.manager.payments.model.payments;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PaymentFactoryTest {

    @Test
    void shouldMarkPaymentActiveWhenStartDateIsToday() {
        LocalDate today = LocalDate.now();

        Payment payment = PaymentFactory.build(10, "name", "desc", today, today.plusDays(30), 30);

        assertThat(payment.status()).isEqualTo(PaymentStatus.ACTIVE);
    }

    @Test
    void shouldMarkPaymentInactiveWhenStartDateIsTomorrow() {
        LocalDate today = LocalDate.now();

        Payment payment = PaymentFactory.build(10, "name", "desc", today.plusDays(1), today.plusDays(30), 30);

        assertThat(payment.status()).isEqualTo(PaymentStatus.INACTIVE);
    }

}
