package com.manager.payments.application.port.in;

import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;

import java.util.UUID;

public interface UpdateReceiptStatusUseCase {

    Receipt updateStatus(UUID receiptId, ReceiptStatus receiptStatus);

}
