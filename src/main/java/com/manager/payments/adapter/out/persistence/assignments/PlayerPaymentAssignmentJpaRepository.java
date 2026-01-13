package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.players.PlayerStatus;
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
                where a.player.status = :playerStatus
                  and a.payment.status = :paymentStatus
                  and (a.payment.startDate <= :date or a.payment.startDate is null)
            """)
    List<PlayerPaymentAssignmentJpaEntity> findAllForBilling(PlayerStatus playerStatus, PaymentStatus paymentStatus,
                                                             LocalDate date);

    Page<PlayerPaymentAssignmentJpaEntity> findAllByPlayer_Id(UUID playerId, Pageable pageable);
}
