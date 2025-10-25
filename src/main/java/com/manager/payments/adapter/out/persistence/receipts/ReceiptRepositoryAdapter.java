package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReceiptRepositoryAdapter implements ReceiptRepository {

    private final ReceiptJpaRepository receiptJpaRepository;
    private final ReceiptMapper receiptMapper;

    public ReceiptRepositoryAdapter(ReceiptJpaRepository receiptJpaRepository, ReceiptMapper receiptMapper) {
        this.receiptJpaRepository = receiptJpaRepository;
        this.receiptMapper = receiptMapper;
    }

    @Override
    public Optional<Receipt> findById(UUID id) {
        Optional<ReceiptJpaEntity> receiptJpaEntity = receiptJpaRepository.findById(id);
        return receiptJpaEntity.map(receiptMapper::toReceipt);
    }

    @Override
    public List<Receipt> findAllPendingWithExpirationDateBefore(LocalDate date) {
        List<ReceiptJpaEntity> receipts =
                receiptJpaRepository.findAllByStatusAndExpiryDateBefore(ReceiptStatus.PENDING, date);
        return receipts.stream().map(receiptMapper::toReceipt).toList();
    }

    @Override
    public Receipt save(Receipt receipt) {
        ReceiptJpaEntity receiptJpaEntity = receiptMapper.toReceiptJpaEntity(receipt);
        ReceiptJpaEntity savedReceipt = receiptJpaRepository.save(receiptJpaEntity);
        return receiptMapper.toReceipt(savedReceipt);
    }

    @Override
    public List<Receipt> saveAll(List<Receipt> receipts) {
        List<ReceiptJpaEntity> receiptJpaEntities =
                receipts.stream().map(receiptMapper::toReceiptJpaEntity).toList();
        List<ReceiptJpaEntity> savedEntities = receiptJpaRepository.saveAll(receiptJpaEntities);
        return savedEntities.stream().map(receiptMapper::toReceipt).toList();
    }

    @Override
    public Receipt updateStatus(UUID receiptId, ReceiptStatus status) {
        ReceiptJpaEntity receipt =
                receiptJpaRepository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        receipt.setStatus(status);
        return receiptMapper.toReceipt(receiptJpaRepository.save(receipt));
    }

    @Override
    public boolean existsByPlayerPaymentAndPeriod(Receipt receipt) {
        return receiptJpaRepository.existsByPlayer_IdAndPayment_IdAndPeriodStartDateAndPeriodEndDate(receipt.player().id(), receipt.payment().id(), receipt.periodStartDate(), receipt.periodEndDate());
    }

    @Override
    public boolean existsByPlayerAndPayment(Receipt receipt) {
        return receiptJpaRepository.existsByPlayer_IdAndPayment_Id(receipt.player().id(), receipt.payment().id());
    }

    @Override
    public List<Receipt> findAllByPlayerId(UUID playerId) {
        List<ReceiptJpaEntity> receipt = receiptJpaRepository.findAllByPlayer_Id(playerId);
        return receipt.stream().map(receiptMapper::toReceipt).toList();
    }

    @Override
    public List<Receipt> findAllByPaymentId(UUID paymentId) {
        List<ReceiptJpaEntity> receipt = receiptJpaRepository.findAllByPayment_Id(paymentId);
        return receipt.stream().map(receiptMapper::toReceipt).toList();
    }
}
