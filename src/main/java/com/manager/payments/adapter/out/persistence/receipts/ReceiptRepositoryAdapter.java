package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class ReceiptRepositoryAdapter implements ReceiptRepository {

    private final ReceiptJpaRepository receiptJpaRepository;
    private final ReceiptMapper receiptMapper;

    public ReceiptRepositoryAdapter(ReceiptJpaRepository receiptJpaRepository, ReceiptMapper receiptMapper) {
        this.receiptJpaRepository = receiptJpaRepository;
        this.receiptMapper = receiptMapper;
    }

    @Override
    public List<Receipt> findAllPendingWithExpirationDateBefore(LocalDate date) {
        List<ReceiptJpaEntity> receipts =
                receiptJpaRepository.findAllByStatusAndExpiryDateBefore(ReceiptStatus.PENDING, date);
        return receipts.stream().map(receiptMapper::toReceipt).toList();
    }

    @Override
    public Receipt updateStatus(UUID receiptId, ReceiptStatus status) {
        ReceiptJpaEntity receipt =
                receiptJpaRepository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        receipt.setStatus(status);
        return receiptMapper.toReceipt(receiptJpaRepository.save(receipt));
    }
}
