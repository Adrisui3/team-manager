package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.request.UpdateReceiptRequestDTO;
import com.manager.payments.application.port.in.receipts.DeleteReceiptUseCase;
import com.manager.payments.application.port.in.receipts.FindReceiptUseCase;
import com.manager.payments.application.port.in.receipts.ProcessOverdueReceiptsUseCase;
import com.manager.payments.application.port.in.receipts.UpdateReceiptUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.InvalidFilterIntervalException;
import com.manager.payments.model.exceptions.InvalidFilterLimitsException;
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
public class ReceiptService implements ProcessOverdueReceiptsUseCase, UpdateReceiptUseCase,
        DeleteReceiptUseCase, FindReceiptUseCase {

    private final ReceiptRepository repository;

    @Override
    public void processOverdueReceipts(LocalDate date) {
        List<Receipt> overdueReceipts = repository.findAllExpired(date);
        List<Receipt> processedReceipts = OverdueReceiptProcessor.process(overdueReceipts);
        log.info("{} receipts have been marked as OVERDUE", processedReceipts.size());
        repository.saveAll(processedReceipts);
    }

    @Override
    public Receipt update(UUID receiptId, UpdateReceiptRequestDTO request) {
        Receipt receipt = repository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        Receipt updatedReceipt = receipt.update(request.amount(), request.expiryDate(), request.status());

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
    public Page<Receipt> findAll(String query, ReceiptStatus status, LocalDate startDate, LocalDate endDate,
                                 Pageable pageable) {
        if ((startDate == null) ^ (endDate == null)) {
            throw new InvalidFilterIntervalException();
        }

        if (startDate != null && startDate.isAfter(endDate)) {
            throw new InvalidFilterLimitsException();
        }

        return repository.findAll(query.trim().toLowerCase(Locale.ROOT), status, startDate, endDate, pageable);
    }
}
