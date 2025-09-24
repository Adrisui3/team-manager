package com.manager.payments.application.port.in;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.service.PlayerService;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerMinInfo;
import com.manager.payments.model.players.PlayerStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AssignPaymentToPlayerUseCaseTest {


    @Test
    void shouldAssignPaymentToPlayerWhenNoPreviousAssignmentExist() {
        // given
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment(paymentId, 10, "", "", LocalDate.now(), LocalDate.now(),
                LocalDate.now(), 10, PaymentStatus.ACTIVE, new ArrayList<>());
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase = new PlayerService(paymentRepository,
                playerRepository);

        // when
        player = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);

        //then
        assertThat(player.payments().size()).isEqualTo(1);
        assertThat(payment.players().size()).isEqualTo(1);
        assertThat(player.payments().contains(PaymentMinInfo.from(payment))).isTrue();
        assertThat(payment.players().contains(PlayerMinInfo.from(player))).isTrue();
    }

    @Test
    void shouldAssignPaymentToPlayerAndCreateProratedReceipt() {
        // given
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment(paymentId, 10, "", "", LocalDate.now(), LocalDate.now().plusDays(5),
                LocalDate.now(), 10, PaymentStatus.ACTIVE, new ArrayList<>());
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase = new PlayerService(paymentRepository,
                playerRepository);

        // when
        player = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);

        //then
        assertThat(player.payments().size()).isEqualTo(1);
        assertThat(payment.players().size()).isEqualTo(1);
        assertThat(player.payments().contains(PaymentMinInfo.from(payment))).isTrue();
        assertThat(payment.players().contains(PlayerMinInfo.from(player))).isTrue();
        assertThat(player.receipts().size()).isEqualTo(1);
        assertThat(player.receipts().getFirst().amount()).isEqualTo(5);
    }

    @Test
    void shouldAssignPaymentToPlayerAndCreateFullReceiptWhenNextDateIsPast() {
        // given
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment(paymentId, 10, "", "", LocalDate.now(), LocalDate.now().minusDays(5),
                LocalDate.now(), 10, PaymentStatus.ACTIVE, new ArrayList<>());
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase = new PlayerService(paymentRepository,
                playerRepository);

        // when
        player = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);

        //then
        assertThat(player.payments().size()).isEqualTo(1);
        assertThat(payment.players().size()).isEqualTo(1);
        assertThat(player.payments().contains(PaymentMinInfo.from(payment))).isTrue();
        assertThat(payment.players().contains(PlayerMinInfo.from(player))).isTrue();
        assertThat(player.receipts().size()).isEqualTo(1);
        assertThat(player.receipts().getFirst().amount()).isEqualTo(10);
    }

    @Test
    void shouldAssignPaymentToPlayerAndNotCreateReceiptIfPaymentInactive() {
        // given
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment(paymentId, 10, "", "", LocalDate.now(), LocalDate.now().minusDays(5),
                LocalDate.now(), 10, PaymentStatus.INACTIVE, new ArrayList<>());
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase = new PlayerService(paymentRepository,
                playerRepository);

        // when
        player = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);

        //then
        assertThat(player.payments().size()).isEqualTo(1);
        assertThat(payment.players().size()).isEqualTo(1);
        assertThat(player.payments().contains(PaymentMinInfo.from(payment))).isTrue();
        assertThat(payment.players().contains(PlayerMinInfo.from(player))).isTrue();
        assertThat(player.receipts().isEmpty()).isTrue();
    }
}
