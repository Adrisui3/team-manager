package com.manager.payments.application.service;

import com.manager.payments.application.port.in.ProcessOverdueReceiptsUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.receipts.OverdueReceiptProcessor;
import com.manager.payments.model.receipts.Receipt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReceiptService implements ProcessOverdueReceiptsUseCase {

    private final ReceiptRepository receiptRepository;

    @Override
    public void processOverdueReceipts(LocalDate date) {
        log.info("Updating overdue receipts at {}", date);
        List<Receipt> overdueReceipts = receiptRepository.findAllPendingWithExpirationDateBefore(date);
        log.info("Found {} overdue receipts", overdueReceipts.size());
        List<Receipt> processedReceipts = OverdueReceiptProcessor.process(overdueReceipts);
        log.info("Processed {} overdue receipts", overdueReceipts.size());
        receiptRepository.saveAll(processedReceipts);
    }
}
