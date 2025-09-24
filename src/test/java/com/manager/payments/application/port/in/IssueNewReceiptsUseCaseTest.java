package com.manager.payments.application.port.in;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.service.BillingService;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerMinInfo;
import com.manager.payments.model.players.PlayerStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class IssueNewReceiptsUseCaseTest {

    @Test
    void shouldCreateNewReceiptForEachPlayer() {
        // given
        UUID player1Id = UUID.randomUUID();
        Player player1 = new Player(player1Id, "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        UUID player2Id = UUID.randomUUID();
        Player player2 = new Player(player2Id, "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment(paymentId, 10, "", "", LocalDate.now(), LocalDate.now(),
                LocalDate.now().plusDays(10), 10, PaymentStatus.ACTIVE, new ArrayList<>());
        player1.payments().add(PaymentMinInfo.from(payment));
        player2.payments().add(PaymentMinInfo.from(payment));
        payment.players().add(PlayerMinInfo.from(player1));
        payment.players().add(PlayerMinInfo.from(player2));

        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        Mockito.when(playerRepository.findById(player1Id)).thenReturn(Optional.of(player1));
        Mockito.when(playerRepository.save(player1)).thenReturn(player1);
        Mockito.when(playerRepository.findById(player2Id)).thenReturn(Optional.of(player2));
        Mockito.when(playerRepository.save(player2)).thenReturn(player2);

        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Mockito.when(paymentRepository.findAllActiveAndNextPaymentDateBeforeOrEqual(Mockito.any())).thenReturn(List.of(payment));

        IssueNewReceiptsUseCase issueNewReceiptsUseCase = new BillingService(playerRepository, paymentRepository);

        // when
        issueNewReceiptsUseCase.issueNewReceipts(LocalDate.now());

        // then
        assertThat(player1.receipts().size()).isEqualTo(1);
        assertThat(player1.receipts().getLast().issuedDate()).isEqualTo(LocalDate.now());
        assertThat(player1.receipts().getLast().expiryDate()).isEqualTo(LocalDate.now().plusDays(15));
        assertThat(player2.receipts().size()).isEqualTo(1);
        assertThat(player2.receipts().getLast().issuedDate()).isEqualTo(LocalDate.now());
        assertThat(player2.receipts().getLast().expiryDate()).isEqualTo(LocalDate.now().plusDays(15));
    }

    @Test
    void shouldCreateFullAmountReceiptsForOverduePayments() {
        // given
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "", "", "", "", LocalDate.now(), Category.SENIOR,
                PlayerStatus.ENABLED, new ArrayList<>(), new ArrayList<>());
        Payment overduePayment = new Payment(UUID.randomUUID(), 25.5, "", "", LocalDate.now().minusDays(90),
                LocalDate.now().minusDays(40), LocalDate.now().plusDays(90), 30, PaymentStatus.ACTIVE,
                new ArrayList<>());
        player.payments().add(PaymentMinInfo.from(overduePayment));
        overduePayment.players().add(PlayerMinInfo.from(player));

        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(playerRepository.save(player)).thenReturn(player);

        PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
        Mockito.when(paymentRepository.findAllActiveAndNextPaymentDateBeforeOrEqual(Mockito.any())).thenReturn(
                List.of(overduePayment));

        IssueNewReceiptsUseCase issueNewReceiptsUseCase = new BillingService(playerRepository, paymentRepository);

        // when
        LocalDate today = LocalDate.now();
        issueNewReceiptsUseCase.issueNewReceipts(today);

        // then
        assertThat(player.receipts().size()).isEqualTo(2);
        assertThat(player.receipts().get(0).amount()).isEqualTo(overduePayment.amount());
        assertThat(player.receipts().get(1).amount()).isEqualTo(overduePayment.amount());

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        Mockito.verify(paymentRepository).save(paymentCaptor.capture());
        assertThat(paymentCaptor.getValue().nextPaymentDate().isAfter(today)).isTrue();
    }

}
