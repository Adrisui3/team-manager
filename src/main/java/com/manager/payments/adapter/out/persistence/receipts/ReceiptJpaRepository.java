package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReceiptJpaRepository extends JpaRepository<ReceiptJpaEntity, UUID> {
    List<ReceiptJpaEntity> findAllByStatusAndExpiryDateBefore(ReceiptStatus status, LocalDate expiryDateBefore);

    boolean existsByPlayerPaymentAssignment_IdAndPeriodStartDateAndPeriodEndDate(UUID playerPaymentAssignmentId,
                                                                                 LocalDate startDate,
                                                                                 LocalDate endDate);

}
