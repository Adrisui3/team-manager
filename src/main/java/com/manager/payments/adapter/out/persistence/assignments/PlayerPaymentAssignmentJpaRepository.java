package com.manager.payments.adapter.out.persistence.assignments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PlayerPaymentAssignmentJpaRepository extends JpaRepository<PlayerPaymentAssignmentJpaEntity, UUID> {

    boolean existsByPlayer_IdAndPayment_Id(UUID playerId, UUID paymentId);

    void deleteByPlayer_IdAndPayment_Id(UUID playerId, UUID paymentId);

    @Query("""
                select a
                from PlayerPaymentAssignmentJpaEntity a
                where a.player.status = 'ENABLED'
                  and a.payment.status = 'ACTIVE'
                  and (
                        :currentDate between a.payment.startDate and a.payment.endDate
                        or a.payment.periodicity = 'ONCE'
                      )
            """)
    List<PlayerPaymentAssignmentJpaEntity> findAllForBilling(LocalDate currentDate);

    Page<PlayerPaymentAssignmentJpaEntity> findAllByPlayer_Id(UUID playerId, Pageable pageable);
}
