package com.manager.payments.adapter.in.rest.jobs;

import com.manager.payments.application.port.in.CreateReceiptUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.PlayerMinInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class BillingJob {

    private final Logger logger = LoggerFactory.getLogger(BillingJob.class);
    private final CreateReceiptUseCase createReceiptUseCase;
    private final PaymentRepository paymentRepository;

    public BillingJob(CreateReceiptUseCase createReceiptUseCase, PaymentRepository paymentRepository) {
        this.createReceiptUseCase = createReceiptUseCase;
        this.paymentRepository = paymentRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void billing() {
        LocalDate now = LocalDate.now();
        logger.info("Running BillingJob at {}", now);
        List<Payment> payments = paymentRepository.findAllActiveAndNextPaymentDateBefore(now);
        logger.info("Processing {} payments", payments.size());
        for (Payment payment : payments) {
            LocalDate newNextPaymentDate = payment.nextPaymentDate().plusDays(payment.periodDays());
            paymentRepository.updateNextPaymentDate(payment.id(), newNextPaymentDate);

            for (PlayerMinInfo player : payment.players()) {
                ReceiptMinInfo newReceipt = createReceiptUseCase.createReceipt(player.id(), payment.id());
                logger.info("Created receipt {} for player {}", newReceipt.id(), player.id());
            }
        }
    }
}
