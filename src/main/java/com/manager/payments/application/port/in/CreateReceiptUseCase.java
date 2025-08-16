package com.manager.payments.application.port.in;

import com.manager.payments.model.receipts.Receipt;

import java.util.UUID;

public interface CreateReceiptUseCase {
    Receipt createReceipt(UUID userId, UUID paymentId);
}
