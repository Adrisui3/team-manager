package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReceiptJpaRepository extends JpaRepository<ReceiptJpaEntity, UUID> {

    List<ReceiptJpaEntity> findAllByStatusAndExpiryDateBefore(ReceiptStatus status, LocalDate expiryDateBefore);

    boolean existsByPlayer_IdAndPayment_IdAndPeriodStartDateAndPeriodEndDate(UUID playerId, UUID paymentId,
                                                                             LocalDate startDate, LocalDate endDate);

    List<ReceiptJpaEntity> findAllByPlayer_Id(UUID playerId);

    List<ReceiptJpaEntity> findAllByPayment_Id(UUID paymentId);

    boolean existsByPlayer_IdAndPayment_Id(UUID playerId, UUID paymentId);

    Page<ReceiptJpaEntity> findAllByStatus(Pageable pageable, ReceiptStatus status);
}
