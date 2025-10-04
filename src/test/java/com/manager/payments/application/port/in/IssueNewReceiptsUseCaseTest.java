package com.manager.payments.application.port.in;

import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.application.service.BillingService;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IssueNewReceiptsUseCaseTest {

    @Test
    void shouldIssueOneCompleteReceipt() {
        // given
        Player player = Mockito.mock(Player.class);

        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);
        LocalDate today = LocalDate.of(2025, 9, 1);
        Payment payment = new Payment(UUID.randomUUID(), "CODE", 50, "", "", startDate, endDate, Periodicity.MONTHLY,
                PaymentStatus.ACTIVE);

        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(player, payment, true);
        PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository =
                Mockito.mock(PlayerPaymentAssignmentRepository.class);
        Mockito.when(playerPaymentAssignmentRepository.findAllActiveAndStartDateBeforeOrEqual(any())).thenReturn(List.of(playerPaymentAssignment));

        ReceiptRepository receiptRepository = Mockito.mock(ReceiptRepository.class);
        Mockito.when(receiptRepository.exists(any())).thenReturn(false);

        IssueNewReceiptsUseCase issueNewReceiptsUseCase = new BillingService(playerPaymentAssignmentRepository,
                receiptRepository);

        // when
        issueNewReceiptsUseCase.issueNewReceipts(today);

        // then
        ArgumentCaptor<Receipt> receiptsCaptor = ArgumentCaptor.forClass(Receipt.class);
        verify(receiptRepository).save(receiptsCaptor.capture());
        verify(receiptRepository, times(1)).exists(any());

        Receipt receipt = receiptsCaptor.getValue();
        assertThat(receipt.amount()).isEqualTo(payment.amount());
        assertThat(receipt.periodStartDate()).isEqualTo(LocalDate.of(2025, 9, 1));
        assertThat(receipt.periodEndDate()).isEqualTo(LocalDate.of(2025, 9, 30));
    }

    @Test
    void shouldNotIssueRepeatedReceipts() {
        // given
        Player player = Mockito.mock(Player.class);

        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);
        LocalDate today = LocalDate.of(2025, 11, 16);
        Payment payment = new Payment(UUID.randomUUID(), "CODE", 50, "", "", startDate, endDate, Periodicity.MONTHLY,
                PaymentStatus.ACTIVE);

        PlayerPaymentAssignment playerPaymentAssignment = new PlayerPaymentAssignment(player, payment, true);
        PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository =
                Mockito.mock(PlayerPaymentAssignmentRepository.class);
        Mockito.when(playerPaymentAssignmentRepository.findAllActiveAndStartDateBeforeOrEqual(any())).thenReturn(List.of(playerPaymentAssignment));

        ReceiptRepository receiptRepository = Mockito.mock(ReceiptRepository.class);
        Mockito.when(receiptRepository.exists(any())).thenReturn(true);

        IssueNewReceiptsUseCase issueNewReceiptsUseCase = new BillingService(playerPaymentAssignmentRepository,
                receiptRepository);

        // when
        issueNewReceiptsUseCase.issueNewReceipts(today);

        // then
        verify(receiptRepository, never()).save(any());
        verify(receiptRepository, times(1)).exists(any());
    }

}
