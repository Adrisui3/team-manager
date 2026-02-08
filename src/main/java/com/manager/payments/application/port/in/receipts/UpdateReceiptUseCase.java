package com.manager.payments.application.port.in.receipts;

import com.manager.payments.adapter.in.rest.dto.request.UpdateReceiptRequestDTO;
import com.manager.payments.model.receipts.Receipt;

import java.util.UUID;

public interface UpdateReceiptUseCase {

    Receipt update(UUID receiptId, UpdateReceiptRequestDTO request);
}
