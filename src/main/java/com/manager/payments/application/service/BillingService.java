package com.manager.payments.application.service;

import com.manager.payments.application.port.in.IssueNewReceiptsUseCase;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.billing.BillingProcessor;
import com.manager.payments.model.receipts.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class BillingService implements IssueNewReceiptsUseCase {

    private final Logger logger = LoggerFactory.getLogger(BillingService.class);
    private final PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;
    private final ReceiptRepository receiptRepository;

    public BillingService(PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository,
                          ReceiptRepository receiptRepository) {
        this.playerPaymentAssignmentRepository = playerPaymentAssignmentRepository;
        this.receiptRepository = receiptRepository;
    }

    @Override
    public void issueNewReceipts(LocalDate date) {
        logger.info("Starting BillingJob for {}", date);
        long tIni = System.currentTimeMillis();
        List<PlayerPaymentAssignment> assignments =
                playerPaymentAssignmentRepository.findAllActiveAndStartDateBeforeOrEqual(date);
        logger.info("Processing {} player-payment assignments", assignments.size());
        for (PlayerPaymentAssignment assignment : assignments) {
            Predicate<Receipt> playerExists = switch (assignment.payment().periodicity()) {
                case MONTHLY, QUARTERLY -> receiptRepository::existsByPlayerPaymentAndPeriod;
                case ONCE -> receiptRepository::existsByPlayerAndPayment;
            };
            Optional<Receipt> optionalReceipt = BillingProcessor.process(assignment, date, playerExists);
            optionalReceipt.ifPresent(receipt -> {
                receiptRepository.save(receipt);
                logger.info("Generated receipt for assignment {}", assignment.id());
            });
        }
        logger.info("BillingJob finished in {} milliseconds", System.currentTimeMillis() - tIni);
    }
}
