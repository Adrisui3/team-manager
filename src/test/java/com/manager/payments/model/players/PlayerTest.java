package com.manager.payments.model.players;

import com.manager.payments.model.exceptions.PaymentNotAssignedException;
import com.manager.payments.model.exceptions.PlayerNotAssignedException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.payments.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class PlayerTest {

    @Test
    void shouldCreateReceiptForPlayerWhenPaymentIsAssigned() {

        // given
        Player player = new Player(UUID.randomUUID(), "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        Payment payment = new Payment(UUID.randomUUID(), 10, "", "", LocalDate.now(), LocalDate.now(),
                LocalDate.now(), 10, PaymentStatus.ACTIVE, new ArrayList<>());
        player.payments().add(PaymentMinInfo.from(payment));
        payment.players().add(PlayerMinInfo.from(player));

        // when
        assertThatCode(() -> player.createReceiptFor(payment)).doesNotThrowAnyException();

        // then
        assertThat(player.receipts().size()).isEqualTo(1);
    }

    @Test
    void shouldThrowExceptionWhenPaymentIsNotAssigned() {

        // given
        Player player = new Player(UUID.randomUUID(), "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        Payment payment = new Payment(UUID.randomUUID(), 10, "", "", LocalDate.now(), LocalDate.now(),
                LocalDate.now(), 10, PaymentStatus.ACTIVE, new ArrayList<>());
        payment.players().add(PlayerMinInfo.from(player));

        // when
        assertThatThrownBy(() -> player.createReceiptFor(payment)).isInstanceOf(PaymentNotAssignedException.class);
    }

    @Test
    void shouldThrowExceptionWhenPlayerIsNotAssigned() {

        // given
        Player player = new Player(UUID.randomUUID(), "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        Payment payment = new Payment(UUID.randomUUID(), 10, "", "", LocalDate.now(), LocalDate.now(),
                LocalDate.now(), 10, PaymentStatus.ACTIVE, new ArrayList<>());
        player.payments().add(PaymentMinInfo.from(payment));

        // when
        assertThatThrownBy(() -> player.createReceiptFor(payment)).isInstanceOf(PlayerNotAssignedException.class);
    }

}
