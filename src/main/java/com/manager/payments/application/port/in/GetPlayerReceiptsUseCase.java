package com.manager.payments.application.port.in;

import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetPlayerReceiptsUseCase {

    Page<Receipt> getPlayerReceipts(UUID playerId, ReceiptStatus status, Pageable pageable);
}
