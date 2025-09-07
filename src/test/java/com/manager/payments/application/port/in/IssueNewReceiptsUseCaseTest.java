package com.manager.payments.application.port.in;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.service.BillingService;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.users.Category;
import com.manager.payments.model.users.Player;
import com.manager.payments.model.users.PlayerMinInfo;
import com.manager.payments.model.users.PlayerStatus;
import org.junit.jupiter.api.Test;
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
                LocalDate.now(), 10, PaymentStatus.ACTIVE, new ArrayList<>());
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
        Mockito.when(paymentRepository.findAllActiveAndNextPaymentDateBefore(Mockito.any())).thenReturn(List.of(payment));

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

}
