package com.manager.payments.application.port.in.receipts;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IssueNewReceiptsUseCaseIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private IssueNewReceiptsUseCase issueNewReceiptsUseCase;

    @Test
    void shouldIssueOneCompleteReceipt() {
        LocalDate today = LocalDate.of(2025, 9, 1);
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment()
                .id(null)
                .endDate(LocalDate.of(2026, 6, 30))
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                .build());
        playerPaymentAssignmentRepository.save(PlayerPaymentAssignment.builder()
                .player(player)
                .payment(payment)
                .build());

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

        Page<Receipt> receipts = receiptRepository.findAllByPlayerId(player.id(), PageRequest.of(0, 10));
        assertThat(receipts.getTotalElements()).isEqualTo(1);
        assertThat(receipts.getContent().getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "player.createdAt", "player.updatedAt",
                        "payment.createdAt", "payment.updatedAt")
                .isEqualTo(expected);
    }

    @Test
    void shouldIssueHalfAReceipt() {
        LocalDate today = LocalDate.of(2025, 9, 16);
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment()
                .id(null)
                .endDate(LocalDate.of(2026, 6, 30))
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                .build());
        playerPaymentAssignmentRepository.save(PlayerPaymentAssignment.builder()
                .player(player)
                .payment(payment)
                .build());

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

        Page<Receipt> receipts = receiptRepository.findAllByPlayerId(player.id(), PageRequest.of(0, 10));
        assertThat(receipts.getTotalElements()).isEqualTo(1);
        assertThat(receipts.getContent().getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "player.createdAt", "player.updatedAt",
                        "payment.createdAt", "payment.updatedAt")
                .isEqualTo(expected);
    }

    @Test
    void shouldNotIssueRepeatedReceipts() {
        LocalDate today = LocalDate.of(2025, 11, 16);
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment()
                .id(null)
                .endDate(LocalDate.of(2026, 6, 30))
                .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                .build());
        playerPaymentAssignmentRepository.save(PlayerPaymentAssignment.builder()
                .player(player)
                .payment(payment)
                .build());
        receiptRepository.save(ReceiptGenerator.receipt()
                .code("12345678-PAY-001-112025")
                .periodStartDate(LocalDate.of(2025, 11, 1))
                .periodEndDate(LocalDate.of(2025, 11, 30))
                .player(player)
                .payment(payment)
                .build());

        issueNewReceiptsUseCase.issueNewReceipts(today);

        Page<Receipt> receipts = receiptRepository.findAllByPlayerId(player.id(), PageRequest.of(0, 10));
        assertThat(receipts.getTotalElements()).isEqualTo(1);
    }
}
