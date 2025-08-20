package com.manager.payments.application.service;

import com.manager.payments.application.port.in.CreateReceiptUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.Player;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BillingService implements CreateReceiptUseCase {

    private final PlayerRepository playerRepository;
    private final PaymentRepository paymentRepository;

    public BillingService(PlayerRepository playerRepository, PaymentRepository paymentRepository) {
        this.playerRepository = playerRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ReceiptMinInfo createReceipt(UUID playerId, UUID paymentId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));

        player.createReceiptFor(payment);
        Player updatedPlayer = playerRepository.save(player);
        return updatedPlayer.receipts().getLast();
    }
}
