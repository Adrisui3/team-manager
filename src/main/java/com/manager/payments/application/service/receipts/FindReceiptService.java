package com.manager.payments.application.service.receipts;

import com.manager.payments.application.port.in.receipts.FindReceiptUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.InvalidFilterIntervalException;
import com.manager.payments.model.exceptions.InvalidFilterLimitsException;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FindReceiptService implements FindReceiptUseCase {

    private final ReceiptRepository repository;

    @Override
    public Receipt findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ReceiptNotFoundException(id));
    }

    @Override
    public Page<Receipt> findAll(String query, ReceiptStatus status, LocalDate startDate, LocalDate endDate,
                                 Pageable pageable) {
        if ((startDate == null) ^ (endDate == null)) {
            throw new InvalidFilterIntervalException();
        }

        if (startDate != null && startDate.isAfter(endDate)) {
            throw new InvalidFilterLimitsException();
        }

        return repository.findAll(query.trim().toLowerCase(Locale.ROOT), status, startDate, endDate, pageable);
    }
}
