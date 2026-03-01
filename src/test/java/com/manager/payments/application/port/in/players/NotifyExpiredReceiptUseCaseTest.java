package com.manager.payments.application.port.in.players;

import com.manager.email.application.port.in.SendExpiredReceiptEmailUseCase;
import com.manager.email.generator.ExpiredReceiptEmailRequestGenerator;
import com.manager.email.model.ExpiredReceiptEmailRequest;
import com.manager.payments.application.port.in.receipts.NotifyExpiredReceiptUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.generator.PaymentGenerator;
import com.manager.payments.generator.PlayerGenerator;
import com.manager.payments.generator.ReceiptGenerator;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NotifyExpiredReceiptUseCaseTest {

    @MockitoBean
    private ReceiptRepository receiptRepository;

    @MockitoBean
    private SendExpiredReceiptEmailUseCase sendExpiredReceiptEmailUseCase;

    @Autowired
    private NotifyExpiredReceiptUseCase notifyExpiredReceiptUseCase;

    @Test
    void shouldNotifyPlayersOfExpiredReceipt() {
        Player player = PlayerGenerator.player().build();
        Payment payment = PaymentGenerator.payment().build();

        UUID receiptId = UUID.randomUUID();
        Receipt receipt = ReceiptGenerator.receipt().id(receiptId).status(ReceiptStatus.OVERDUE)
                .player(player).payment(payment).build();
        when(receiptRepository.findById(receiptId)).thenReturn(Optional.of(receipt));

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

        notifyExpiredReceiptUseCase.notifyExpiredReceipts(List.of(receiptId));

        verify(sendExpiredReceiptEmailUseCase).sendExpiredReceiptEmail(expectedRequest);
    }

    @Test
    void shouldNotifyWithMultipleExpired() {
        Player player = PlayerGenerator.player().build();
        Payment payment = PaymentGenerator.payment().build();

        UUID receiptId1 = UUID.randomUUID();
        UUID receiptId2 = UUID.randomUUID();
        UUID receiptId3 = UUID.randomUUID();
        Receipt receipt1 = ReceiptGenerator.receipt().id(receiptId1).status(ReceiptStatus.OVERDUE)
                .player(player).payment(payment).build();
        Receipt receipt2 = ReceiptGenerator.receipt().id(receiptId2).status(ReceiptStatus.OVERDUE)
                .player(player).payment(payment).build();
        Receipt receipt3 = ReceiptGenerator.receipt().id(receiptId3).status(ReceiptStatus.OVERDUE)
                .player(player).payment(payment).build();
        when(receiptRepository.findById(receiptId1)).thenReturn(Optional.of(receipt1));
        when(receiptRepository.findById(receiptId2)).thenReturn(Optional.of(receipt2));
        when(receiptRepository.findById(receiptId3)).thenReturn(Optional.of(receipt3));

        notifyExpiredReceiptUseCase.notifyExpiredReceipts(List.of(receiptId1, receiptId2, receiptId3));

        verify(sendExpiredReceiptEmailUseCase, times(3)).sendExpiredReceiptEmail(any(ExpiredReceiptEmailRequest.class));
    }

    @Test
    void shouldNotNotifyPlayersOfNotExpired() {
        UUID receiptId = UUID.randomUUID();
        Receipt receipt = ReceiptGenerator.receipt().id(receiptId).status(ReceiptStatus.PENDING).build();
        when(receiptRepository.findById(receiptId)).thenReturn(Optional.of(receipt));

        notifyExpiredReceiptUseCase.notifyExpiredReceipts(List.of(receiptId));

        verify(sendExpiredReceiptEmailUseCase, never()).sendExpiredReceiptEmail(any(ExpiredReceiptEmailRequest.class));
    }

    @Test
    void shouldNotFailWhenOneReceiptDoesNotExist() {
        Player player = PlayerGenerator.player().build();
        Payment payment = PaymentGenerator.payment().build();

        UUID receiptId1 = UUID.randomUUID();
        UUID receiptId2 = UUID.randomUUID();
        UUID missingId = UUID.randomUUID();
        Receipt receipt1 = ReceiptGenerator.receipt().id(receiptId1).status(ReceiptStatus.OVERDUE)
                .player(player).payment(payment).build();
        Receipt receipt2 = ReceiptGenerator.receipt().id(receiptId2).status(ReceiptStatus.OVERDUE)
                .player(player).payment(payment).build();
        when(receiptRepository.findById(receiptId1)).thenReturn(Optional.of(receipt1));
        when(receiptRepository.findById(missingId)).thenReturn(Optional.empty());
        when(receiptRepository.findById(receiptId2)).thenReturn(Optional.of(receipt2));

        notifyExpiredReceiptUseCase.notifyExpiredReceipts(List.of(receiptId1, missingId, receiptId2));

        verify(sendExpiredReceiptEmailUseCase, times(2)).sendExpiredReceiptEmail(any(ExpiredReceiptEmailRequest.class));
    }
}
