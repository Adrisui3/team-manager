package com.manager.payments.adapter.in.rest.jobs;

import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReceiptJob {

    private final Logger logger = LoggerFactory.getLogger(ReceiptJob.class);
    private final ReceiptRepository receiptRepository;

    public ReceiptJob(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateOverdueReceipts() {
        LocalDate now = LocalDate.now();
        logger.info("Updating overdue receipts at {}", now);
        List<Receipt> overdueReceipts = receiptRepository.findAllPendingWithExpirationDateBefore(now);
        logger.info("Found {} overdue receipts", overdueReceipts.size());
        for (Receipt receipt : overdueReceipts) {
            receiptRepository.updateStatus(receipt.id(), ReceiptStatus.OVERDUE);
            logger.info("Receipt {} is now overdue", receipt.id());
        }
    }
}
