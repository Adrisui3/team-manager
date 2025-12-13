package com.manager.payments.application.service;

import com.manager.payments.application.port.in.ProcessOverdueReceiptsUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.receipts.OverdueReceiptProcessor;
import com.manager.payments.model.receipts.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
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
        List<Receipt> processedReceipts = OverdueReceiptProcessor.process(overdueReceipts);
        logger.info("Processed {} overdue receipts", overdueReceipts.size());
        receiptRepository.saveAll(processedReceipts);
    }
}
