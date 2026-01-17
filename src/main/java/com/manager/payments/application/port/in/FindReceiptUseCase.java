package com.manager.payments.application.port.in;

import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FindReceiptUseCase {

    Receipt findById(UUID id);

    Page<Receipt> findAllByQuery(String query, ReceiptStatus status, Pageable pageable);
}
