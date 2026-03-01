package com.manager.payments.application.service.receipts;

import com.manager.payments.application.port.in.receipts.ProcessOverdueReceiptsUseCase;
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
public class ProcessOverdueReceiptsService implements ProcessOverdueReceiptsUseCase {

    private final ReceiptRepository repository;

    @Override
    public void processOverdueReceipts(LocalDate date) {
        List<Receipt> overdueReceipts = repository.findAllExpired(date);
        List<Receipt> processedReceipts = OverdueReceiptProcessor.process(overdueReceipts);
        log.info("{} receipts have been marked as OVERDUE", processedReceipts.size());
        repository.saveAll(processedReceipts);
    }
}
