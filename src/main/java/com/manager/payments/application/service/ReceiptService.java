package com.manager.payments.application.service;

import com.manager.email.application.port.in.SendExpiredReceiptEmailUseCase;
import com.manager.email.model.ExpiredReceiptEmailRequest;
import com.manager.payments.adapter.in.rest.dto.request.UpdateReceiptRequestDTO;
import com.manager.payments.application.port.in.receipts.*;
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
@RequiredArgsConstructor
@Slf4j
public class ReceiptService implements ProcessOverdueReceiptsUseCase, UpdateReceiptUseCase,
        DeleteReceiptUseCase, FindReceiptUseCase, NotifyExpiredReceiptUseCase {

    private final ReceiptRepository repository;
    private final SendExpiredReceiptEmailUseCase sendExpiredReceiptEmailUseCase;

    @Override
    @Transactional
    public void processOverdueReceipts(LocalDate date) {
        List<Receipt> overdueReceipts = repository.findAllExpired(date);
        List<Receipt> processedReceipts = OverdueReceiptProcessor.process(overdueReceipts);
        log.info("{} receipts have been marked as OVERDUE", processedReceipts.size());
        repository.saveAll(processedReceipts);
    }

    @Override
    @Transactional
    public Receipt update(UUID receiptId, UpdateReceiptRequestDTO request) {
        Receipt receipt = repository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        Receipt updatedReceipt = receipt.update(request.amount(), request.expiryDate(), request.status());

        return repository.save(updatedReceipt);
    }

    @Override
    @Transactional
    public void deleteReceipt(UUID receiptId) {
        if (!repository.existsById(receiptId)) {
            throw new ReceiptNotFoundException(receiptId);
        }

        repository.deleteById(receiptId);
    }

    @Override
    @Transactional
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

    @Override
    public void notifyExpiredReceipts(List<UUID> expiredReceiptsIds) {
        expiredReceiptsIds.forEach(receiptId -> {
            try {
                notifyExpiredReceipt(receiptId);
            } catch (ReceiptNotFoundException e) {
                log.error("Receipt with id {} was not found", receiptId);
            }
        });
    }

    private void notifyExpiredReceipt(UUID receiptId) {
        Receipt receipt = repository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        if (receipt.status() != ReceiptStatus.OVERDUE) {
            log.warn("Skipping notification for receipt {}, status: {}", receipt.id(), receipt.status());
            return;
        }

        ExpiredReceiptEmailRequest request = ExpiredReceiptEmailRequest.buildFromReceipt(receipt);
        sendExpiredReceiptEmailUseCase.sendExpiredReceiptEmail(request);
    }
}
