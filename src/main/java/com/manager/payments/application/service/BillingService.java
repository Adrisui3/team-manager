package com.manager.payments.application.service;

import com.manager.payments.application.port.in.receipts.IssueNewReceiptsUseCase;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.billing.BillingProcessor;
import com.manager.payments.model.receipts.Receipt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BillingService implements IssueNewReceiptsUseCase {

    private final PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;
    private final ReceiptRepository receiptRepository;

    @Override
    public void issueNewReceipts(LocalDate date) {
        List<PlayerPaymentAssignment> assignments =
                playerPaymentAssignmentRepository.findAllForBilling(date);
        for (PlayerPaymentAssignment assignment : assignments) {
            Predicate<Receipt> playerExists = switch (assignment.payment().periodicity()) {
                case MONTHLY, QUARTERLY -> receiptRepository::existsByPlayerPaymentAndPeriod;
                case ONCE -> receiptRepository::existsByPlayerAndPayment;
            };
            Optional<Receipt> optionalReceipt = BillingProcessor.process(assignment, date, playerExists);
            optionalReceipt.ifPresent(receipt -> {
                receiptRepository.save(receipt);
                log.info("Generated receipt for player {}", assignment.player().id());
            });
        }
    }
}
