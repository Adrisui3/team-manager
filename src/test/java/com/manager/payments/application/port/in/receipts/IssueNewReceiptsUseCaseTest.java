package com.manager.payments.application.port.in.receipts;

import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.generator.PaymentGenerator;
import com.manager.payments.generator.PlayerGenerator;
import com.manager.payments.generator.ReceiptGenerator;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class IssueNewReceiptsUseCaseTest {

    @MockitoBean
    private PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;

    @MockitoBean
    private ReceiptRepository receiptRepository;

    @Autowired
    private IssueNewReceiptsUseCase issueNewReceiptsUseCase;

    @Captor
    private ArgumentCaptor<Receipt> captor;

    @Test
    void shouldIssueOneCompleteReceipt() {
        LocalDate today = LocalDate.of(2025, 9, 1);
        Player player = PlayerGenerator.player().build();
        Payment payment = PaymentGenerator.payment()
                .endDate(LocalDate.of(2026, 6, 30))
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                .build();
        PlayerPaymentAssignment assignment = PlayerPaymentAssignment.builder()
                .player(player)
                .payment(payment)
                .build();

        when(playerPaymentAssignmentRepository.findAllForBilling(any())).thenReturn(List.of(assignment));
        when(receiptRepository.existsByPlayerPaymentAndPeriod(any())).thenReturn(false);

        issueNewReceiptsUseCase.issueNewReceipts(today);

        Receipt expected = ReceiptGenerator.receipt()
                .code("12345678-PAY-001-092025")
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                .issuedDate(LocalDate.of(2025, 9, 1))
                .expiryDate(LocalDate.of(2025, 9, 16))
                .periodStartDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2025, 9, 30))
                .status(ReceiptStatus.PENDING)
                .player(player)
                .payment(payment)
                .build();

        verify(receiptRepository).save(captor.capture());
        verify(receiptRepository, times(1)).existsByPlayerPaymentAndPeriod(any());
        assertThat(captor.getValue()).usingRecursiveComparison().ignoringFields("createdAt").isEqualTo(expected);
    }

    @Test
    void shouldIssueHalfAReceipt() {
        LocalDate today = LocalDate.of(2025, 9, 16);
        Player player = PlayerGenerator.player().build();
        Payment payment = PaymentGenerator.payment()
                .endDate(LocalDate.of(2026, 6, 30))
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                .build();
        PlayerPaymentAssignment assignment = PlayerPaymentAssignment.builder()
                .player(player)
                .payment(payment)
                .build();

        when(playerPaymentAssignmentRepository.findAllForBilling(any())).thenReturn(List.of(assignment));
        when(receiptRepository.existsByPlayerPaymentAndPeriod(any())).thenReturn(false);

        issueNewReceiptsUseCase.issueNewReceipts(today);

        Receipt expected = ReceiptGenerator.receipt()
                .code("12345678-PAY-001-092025")
                .amount(BigDecimal.valueOf(25).setScale(2, RoundingMode.HALF_UP))
                .issuedDate(LocalDate.of(2025, 9, 16))
                .expiryDate(LocalDate.of(2025, 10, 1))
                .periodStartDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2025, 9, 30))
                .status(ReceiptStatus.PENDING)
                .player(player)
                .payment(payment)
                .build();

        verify(receiptRepository).save(captor.capture());
        verify(receiptRepository, times(1)).existsByPlayerPaymentAndPeriod(any());
        assertThat(captor.getValue()).usingRecursiveComparison().ignoringFields("createdAt").isEqualTo(expected);
    }

    @Test
    void shouldNotIssueRepeatedReceipts() {
        LocalDate today = LocalDate.of(2025, 11, 16);
        Player player = PlayerGenerator.player().build();
        Payment payment = PaymentGenerator.payment()
                .endDate(LocalDate.of(2026, 6, 30))
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                .build();
        PlayerPaymentAssignment assignment = PlayerPaymentAssignment.builder()
                .player(player)
                .payment(payment)
                .build();

        when(playerPaymentAssignmentRepository.findAllForBilling(any())).thenReturn(List.of(assignment));
        when(receiptRepository.existsByPlayerPaymentAndPeriod(any())).thenReturn(true);

        issueNewReceiptsUseCase.issueNewReceipts(today);

        verify(receiptRepository, never()).save(any());
        verify(receiptRepository, times(1)).existsByPlayerPaymentAndPeriod(any());
    }
}
