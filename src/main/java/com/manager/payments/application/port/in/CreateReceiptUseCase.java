package com.manager.payments.application.port.in;

import com.manager.payments.model.receipts.ReceiptMinInfo;

import java.util.UUID;

public interface CreateReceiptUseCase {
    ReceiptMinInfo createReceipt(UUID userId, UUID paymentId);
}
