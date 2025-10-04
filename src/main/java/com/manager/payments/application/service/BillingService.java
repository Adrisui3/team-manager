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
        logger.info("Running BillingJob at {}", date);
        List<PlayerPaymentAssignment> assignments =
                playerPaymentAssignmentRepository.findAllActiveAndStartDateBeforeOrEqual(date);
        logger.info("Found {} assignment", assignments.size());
        for (PlayerPaymentAssignment assignment : assignments) {
            logger.info("Processing assignment {}", assignment.id());
            Optional<Receipt> optionalReceipt = BillingProcessor.process(assignment, date, receiptRepository::exists);
            optionalReceipt.ifPresent(receipt -> {
                playerPaymentAssignmentRepository.addReceipt(assignment.id(), receipt);
                receiptRepository.save(receipt);
                logger.info("Generated receipt for period between {} and {}", receipt.periodStartDate(),
                        receipt.periodEndDate());
            });
        }
    }
}
