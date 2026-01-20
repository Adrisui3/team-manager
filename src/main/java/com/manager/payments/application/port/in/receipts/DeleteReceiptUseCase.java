package com.manager.payments.application.port.in.receipts;

import java.util.UUID;

public interface DeleteReceiptUseCase {
    void deleteReceipt(UUID receiptId);
}
