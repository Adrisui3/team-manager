package com.manager.payments.application.service.receipts;

import com.manager.payments.application.port.in.receipts.DeleteReceiptUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteReceiptService implements DeleteReceiptUseCase {

    private final ReceiptRepository repository;

    @Override
    public void deleteReceipt(UUID receiptId) {
        if (!repository.existsById(receiptId)) {
            throw new ReceiptNotFoundException(receiptId);
        }

        repository.deleteById(receiptId);
    }
}
