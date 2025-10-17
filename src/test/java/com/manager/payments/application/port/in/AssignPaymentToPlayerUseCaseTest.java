package com.manager.payments.application.port.in;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.service.PlayerService;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.exceptions.AssignmentAlreadyExistsException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class AssignPaymentToPlayerUseCaseTest {


    @Test
    public void shouldAssignPaymentToPlayer() {
        //given
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "", "", "", "", LocalDate.now(), Category.SENIOR, PlayerStatus.ENABLED);
        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        UUID paymentId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 30);
        Payment payment = new Payment(paymentId, "", BigDecimal.valueOf(50), "", "", startDate, endDate,
                Periodicity.MONTHLY,
                PaymentStatus.ACTIVE);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository =
                Mockito.mock(PlayerPaymentAssignmentRepository.class);
        Mockito.when(playerPaymentAssignmentRepository.existsByPlayerAndPayment(player, payment)).thenReturn(false);
        Mockito.when(playerPaymentAssignmentRepository.save(any())).then(returnsFirstArg());

        AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase =
                new PlayerService(playerPaymentAssignmentRepository, paymentRepository, playerRepository);

        // when
        PlayerPaymentAssignment savedAssignment = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId,
                paymentId);

        // then
        assertThat(savedAssignment.active()).isTrue();
        verify(playerPaymentAssignmentRepository).save(any());
    }

    @Test
    public void shouldFailIfAssignmentAlreadyExists() {
        //given
        UUID playerId = UUID.randomUUID();
        Player player = Mockito.mock(Player.class);
        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        UUID paymentId = UUID.randomUUID();
        Payment payment = Mockito.mock(Payment.class);
        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository =
                Mockito.mock(PlayerPaymentAssignmentRepository.class);
        Mockito.when(playerPaymentAssignmentRepository.existsByPlayerAndPayment(player, payment)).thenReturn(true);
        Mockito.when(playerPaymentAssignmentRepository.save(any())).then(returnsFirstArg());

        AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase =
                new PlayerService(playerPaymentAssignmentRepository, paymentRepository, playerRepository);

        // when
        assertThatThrownBy(() -> assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId)).isInstanceOf(AssignmentAlreadyExistsException.class);
    }
}
