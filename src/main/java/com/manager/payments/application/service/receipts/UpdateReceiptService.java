package com.manager.payments.application.service.receipts;

import com.manager.payments.adapter.in.rest.dto.request.UpdateReceiptRequestDTO;
import com.manager.payments.application.port.in.receipts.UpdateReceiptUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateReceiptService implements UpdateReceiptUseCase {

    private final ReceiptRepository repository;

    @Override
    public Receipt update(UUID receiptId, UpdateReceiptRequestDTO request) {
        Receipt receipt = repository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        Receipt updatedReceipt = receipt.update(request.amount(), request.expiryDate(), request.status());

        return repository.save(updatedReceipt);
    }
}
