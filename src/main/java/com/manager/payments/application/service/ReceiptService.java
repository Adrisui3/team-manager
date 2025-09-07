package com.manager.payments.application.service;

import com.manager.payments.application.port.in.ProcessOverdueReceiptsUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReceiptService implements ProcessOverdueReceiptsUseCase {

    private final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public void processOverdueReceipts(LocalDate date) {
        logger.info("Updating overdue receipts at {}", date);
        List<Receipt> overdueReceipts = receiptRepository.findAllPendingWithExpirationDateBefore(date);
        logger.info("Found {} overdue receipts", overdueReceipts.size());
        for (Receipt receipt : overdueReceipts) {
            Receipt updatedReceipt = receipt.withStatus(ReceiptStatus.OVERDUE);
            receiptRepository.save(updatedReceipt);
            logger.info("Receipt {} is now flagged as overdue", receipt.id());
        }
    }
}
