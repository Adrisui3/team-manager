package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReceiptRepositoryAdapter implements ReceiptRepository {

    private final ReceiptJpaRepository repository;
    private final ReceiptMapper mapper;

    public ReceiptRepositoryAdapter(ReceiptJpaRepository repository, ReceiptMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Receipt> findById(UUID id) {
        Optional<ReceiptJpaEntity> receiptJpaEntity = repository.findById(id);
        return receiptJpaEntity.map(mapper::toReceipt);
    }

    @Override
    public List<Receipt> findAllExpired(LocalDate date) {
        List<ReceiptJpaEntity> receipts =
                repository.findAllExpired(date);
        return receipts.stream().map(mapper::toReceipt).toList();
    }

    @Override
    public Receipt save(Receipt receipt) {
        ReceiptJpaEntity receiptJpaEntity = mapper.toReceiptJpaEntity(receipt);
        ReceiptJpaEntity savedReceipt = repository.save(receiptJpaEntity);
        return mapper.toReceipt(savedReceipt);
    }

    @Override
    public List<Receipt> saveAll(List<Receipt> receipts) {
        List<ReceiptJpaEntity> receiptJpaEntities =
                receipts.stream().map(mapper::toReceiptJpaEntity).toList();
        List<ReceiptJpaEntity> savedEntities = repository.saveAll(receiptJpaEntities);
        return savedEntities.stream().map(mapper::toReceipt).toList();
    }

    @Override
    public boolean existsByPlayerPaymentAndPeriod(Receipt receipt) {
        return repository.existsByPlayer_IdAndPayment_IdAndPeriodStartDateAndPeriodEndDate(receipt.player().id(),
                receipt.payment().id(), receipt.periodStartDate(), receipt.periodEndDate());
    }

    @Override
    public boolean existsByPlayerAndPayment(Receipt receipt) {
        return repository.existsByPlayer_IdAndPayment_Id(receipt.player().id(), receipt.payment().id());
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public Page<Receipt> findAllByPlayerId(UUID playerId, Pageable pageable) {
        Page<ReceiptJpaEntity> receipts = repository.findAllByPlayer_Id(playerId, pageable);
        return receipts.map(mapper::toReceipt);
    }

    @Override
    public Page<Receipt> findAllByPlayerIdAndStatus(UUID playerId, Pageable pageable, ReceiptStatus status) {
        Page<ReceiptJpaEntity> receipts = repository.findAllByPlayer_IdAndStatus(playerId, status, pageable);
        return receipts.map(mapper::toReceipt);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Receipt> findAll(String query, ReceiptStatus status, LocalDate startDate, LocalDate endDate,
                                 Pageable pageable) {
        Page<ReceiptJpaEntity> receipts = repository.findAll(query, status, startDate, endDate, pageable);
        return receipts.map(mapper::toReceipt);
    }
}
