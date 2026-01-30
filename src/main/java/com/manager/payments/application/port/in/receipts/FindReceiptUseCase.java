package com.manager.payments.application.port.in.receipts;

import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface FindReceiptUseCase {

    Receipt findById(UUID id);

    Page<Receipt> findAll(String query, ReceiptStatus status, LocalDate startDate, LocalDate endDate,
                          Pageable pageable);
}
