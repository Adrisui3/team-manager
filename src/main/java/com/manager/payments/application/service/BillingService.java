package com.manager.payments.application.service;

import com.manager.payments.application.port.in.IssueNewReceiptsUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.Player;
import com.manager.payments.model.users.PlayerMinInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BillingService implements IssueNewReceiptsUseCase {

    private final Logger logger = LoggerFactory.getLogger(BillingService.class);
    private final PlayerRepository playerRepository;
    private final PaymentRepository paymentRepository;

    public BillingService(PlayerRepository playerRepository, PaymentRepository paymentRepository) {
        this.playerRepository = playerRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void issueNewReceipts(LocalDate date) {
        logger.info("Running BillingJob at {}", date);
        List<Payment> payments = paymentRepository.findAllActiveAndNextPaymentDateBefore(date);
        logger.info("Processing {} payments", payments.size());
        for (Payment payment : payments) {
            LocalDate newNextPaymentDate = payment.nextPaymentDate().plusDays(payment.periodDays());
            Payment updatedPayment = paymentRepository.updateNextPaymentDate(payment.id(), newNextPaymentDate);

            for (PlayerMinInfo player : updatedPayment.players()) {
                ReceiptMinInfo newReceipt = createReceipt(player.id(), updatedPayment);
                logger.info("Created receipt {} for player {}", newReceipt.id(), updatedPayment.id());
            }
        }
    }

    private ReceiptMinInfo createReceipt(UUID playerId, Payment payment) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));

        player.createReceiptFor(payment);
        Player updatedPlayer = playerRepository.save(player);
        return updatedPlayer.receipts().getLast();
    }
}
