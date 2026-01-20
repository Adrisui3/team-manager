package com.manager.payments.application.service;

import com.manager.payments.application.port.in.receipts.DeleteReceiptUseCase;
import com.manager.payments.application.port.in.receipts.FindReceiptUseCase;
import com.manager.payments.application.port.in.receipts.ProcessOverdueReceiptsUseCase;
import com.manager.payments.application.port.in.receipts.UpdateReceiptStatusUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.receipts.OverdueReceiptProcessor;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReceiptService implements ProcessOverdueReceiptsUseCase, UpdateReceiptStatusUseCase,
        DeleteReceiptUseCase, FindReceiptUseCase {

    private final ReceiptRepository repository;

    @Override
    public void processOverdueReceipts(LocalDate date) {
        log.info("Updating overdue receipts at {}", date);
        List<Receipt> overdueReceipts = repository.findAllExpired(date);
        log.info("Found {} overdue receipts", overdueReceipts.size());
        List<Receipt> processedReceipts = OverdueReceiptProcessor.process(overdueReceipts);
        log.info("Processed {} overdue receipts", overdueReceipts.size());
        repository.saveAll(processedReceipts);
    }

    @Override
    public Receipt updateStatus(UUID receiptId, ReceiptStatus newStatus) {
        Receipt receipt = repository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        LocalDate paymentDate;
        if (!newStatus.equals(ReceiptStatus.PAID)) {
            paymentDate = null;
        } else {
            paymentDate = receipt.status().equals(ReceiptStatus.PAID) ? receipt.paymentDate() : LocalDate.now();
        }

        Receipt updatedReceipt = receipt.toBuilder()
                .status(newStatus)
                .paymentDate(paymentDate)
                .build();

        return repository.save(updatedReceipt);
    }

    @Override
    public void deleteReceipt(UUID receiptId) {
        if (!repository.existsById(receiptId)) {
            throw new ReceiptNotFoundException(receiptId);
        }

        repository.deleteById(receiptId);
    }

    @Override
    public Receipt findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ReceiptNotFoundException(id));
    }

    @Override
    public Page<Receipt> findAllByQuery(String query, ReceiptStatus status, Pageable pageable) {
        return repository.findByQuery(query.trim().toLowerCase(Locale.ROOT), status, pageable);
    }
}
