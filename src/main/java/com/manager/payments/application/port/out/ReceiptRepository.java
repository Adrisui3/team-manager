package com.manager.payments.application.port.out;

import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptRepository {

    Optional<Receipt> findById(UUID id);

    List<Receipt> findAllExpired(LocalDate date);

    Receipt save(Receipt receipt);

    List<Receipt> saveAll(List<Receipt> receipts);

    boolean existsByPlayerPaymentAndPeriod(Receipt receipt);

    boolean existsByPlayerAndPayment(Receipt receipt);

    boolean existsById(UUID id);

    Page<Receipt> findAllByPlayerId(UUID playerId, Pageable pageable);

    Page<Receipt> findAllByPlayerIdAndStatus(UUID playerId, Pageable pageable, ReceiptStatus status);

    void deleteById(UUID id);

    Page<Receipt> findByQuery(String query, ReceiptStatus status, Pageable pageable);
}
