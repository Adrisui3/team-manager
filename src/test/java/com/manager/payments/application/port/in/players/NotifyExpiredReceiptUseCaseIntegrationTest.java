package com.manager.payments.application.port.in.players;

import com.manager.email.application.port.in.SendExpiredReceiptEmailUseCase;
import com.manager.email.generator.ExpiredReceiptEmailRequestGenerator;
import com.manager.email.model.ExpiredReceiptEmailRequest;
import com.manager.payments.application.port.in.receipts.NotifyExpiredReceiptUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.generator.PaymentGenerator;
import com.manager.payments.generator.PlayerGenerator;
import com.manager.payments.generator.ReceiptGenerator;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotifyExpiredReceiptUseCaseIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @MockitoBean
    private SendExpiredReceiptEmailUseCase sendExpiredReceiptEmailUseCase;

    @Autowired
    private NotifyExpiredReceiptUseCase notifyExpiredReceiptUseCase;

    @Test
    void shouldNotifyPlayersOfExpiredReceipt() {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment().id(null).build());
        Receipt receipt = receiptRepository.save(ReceiptGenerator.receipt()
                .status(ReceiptStatus.OVERDUE)
                .expiryDate(LocalDate.of(2025, 10, 15))
                .periodStartDate(LocalDate.of(2025, 10, 1))
                .periodEndDate(LocalDate.of(2025, 10, 31))
                .player(player)
                .payment(payment)
                .build());

        ExpiredReceiptEmailRequest expectedRequest = ExpiredReceiptEmailRequestGenerator.request()
                .playerName(player.name())
                .playerEmail(player.email())
                .playerPersonalId(player.personalId())
                .paymentName(payment.name())
                .expiryDate(receipt.expiryDate())
                .periodStartDate(receipt.periodStartDate())
                .periodEndDate(receipt.periodEndDate())
                .amount(receipt.amount())
                .build();

        notifyExpiredReceiptUseCase.notifyExpiredReceipts(List.of(receipt.id()));

        verify(sendExpiredReceiptEmailUseCase).sendExpiredReceiptEmail(expectedRequest);
    }

    @Test
    void shouldNotifyWithMultipleExpired() {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment().id(null).build());
        Receipt receipt1 = receiptRepository.save(ReceiptGenerator.receipt()
                .status(ReceiptStatus.OVERDUE).player(player).payment(payment).build());
        Receipt receipt2 = receiptRepository.save(ReceiptGenerator.receipt()
                .status(ReceiptStatus.OVERDUE).player(player).payment(payment).build());
        Receipt receipt3 = receiptRepository.save(ReceiptGenerator.receipt()
                .status(ReceiptStatus.OVERDUE).player(player).payment(payment).build());

        notifyExpiredReceiptUseCase.notifyExpiredReceipts(List.of(receipt1.id(), receipt2.id(), receipt3.id()));

        verify(sendExpiredReceiptEmailUseCase, times(3)).sendExpiredReceiptEmail(any(ExpiredReceiptEmailRequest.class));
    }

    @Test
    void shouldNotNotifyPlayersOfNotExpired() {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment().id(null).build());
        Receipt receipt = receiptRepository.save(ReceiptGenerator.receipt()
                .status(ReceiptStatus.PENDING).player(player).payment(payment).build());

        notifyExpiredReceiptUseCase.notifyExpiredReceipts(List.of(receipt.id()));

        verify(sendExpiredReceiptEmailUseCase, never()).sendExpiredReceiptEmail(any(ExpiredReceiptEmailRequest.class));
    }

    @Test
    void shouldNotFailWhenOneReceiptDoesNotExist() {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment().id(null).build());
        Receipt receipt1 = receiptRepository.save(ReceiptGenerator.receipt()
                .status(ReceiptStatus.OVERDUE).player(player).payment(payment).build());
        Receipt receipt2 = receiptRepository.save(ReceiptGenerator.receipt()
                .status(ReceiptStatus.OVERDUE).player(player).payment(payment).build());
        UUID missingId = UUID.randomUUID();

        notifyExpiredReceiptUseCase.notifyExpiredReceipts(List.of(receipt1.id(), missingId, receipt2.id()));

        verify(sendExpiredReceiptEmailUseCase, times(2)).sendExpiredReceiptEmail(any(ExpiredReceiptEmailRequest.class));
    }
}
