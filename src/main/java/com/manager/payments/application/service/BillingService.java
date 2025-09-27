package com.manager.payments.application.service;

import com.manager.payments.application.port.in.IssueNewReceiptsUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.billing.BillingProcessor;
import com.manager.payments.model.payments.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        List<Payment> payments = paymentRepository.findAllActiveAndNextPaymentDateBeforeOrEqual(date);
        logger.info("Processing {} payments", payments.size());
        for (Payment payment : payments) {
            logger.info("Processing payment {}", payment.id());
            Payment updatedPayment = BillingProcessor.process(payment, date, playerRepository::findById,
                    playerRepository::save);
            paymentRepository.save(updatedPayment);
            logger.info("Finished processing payment {}", payment.id());
        }
    }
}
