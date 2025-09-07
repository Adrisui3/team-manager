package com.manager.payments.application.port.out;

import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReceiptRepository {

    List<Receipt> findAllPendingWithExpirationDateBefore(LocalDate date);

    Receipt updateStatus(UUID receiptId, ReceiptStatus status);
}
