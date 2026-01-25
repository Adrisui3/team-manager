package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReceiptJpaRepository extends JpaRepository<ReceiptJpaEntity, UUID> {

    @Query("""
            select r
            from ReceiptJpaEntity r
            where r.status = 'PENDING' and r.expiryDate < :date
            """)
    List<ReceiptJpaEntity> findAllExpired(@Param("date") LocalDate date);

    boolean existsByPlayer_IdAndPayment_IdAndPeriodStartDateAndPeriodEndDate(UUID playerId, UUID paymentId,
                                                                             LocalDate startDate, LocalDate endDate);

    boolean existsByPlayer_IdAndPayment_Id(UUID playerId, UUID paymentId);

    Page<ReceiptJpaEntity> findAllByPlayer_Id(UUID playerId, Pageable pageable);

    Page<ReceiptJpaEntity> findAllByPlayer_IdAndStatus(UUID playerId, ReceiptStatus status, Pageable pageable);

    @Query("""
                select r
                from ReceiptJpaEntity r
                where lower(r.code) like concat(concat('%', :query), '%')
                      and (:status is null or r.status = :status)
            """)
    Page<ReceiptJpaEntity> findAllByQuery(@Param("query") String query, @Param("status") ReceiptStatus status,
                                          Pageable pageable);
}
