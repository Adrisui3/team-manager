package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.players.PlayerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PlayerPaymentAssignmentJpaRepository extends JpaRepository<PlayerPaymentAssignmentJpaEntity, UUID> {

    boolean existsByPlayer_IdAndPayment_Id(UUID playerId, UUID paymentId);

    void deleteByPlayer_IdAndPayment_Id(UUID playerId, UUID paymentId);

    List<PlayerPaymentAssignmentJpaEntity> findAllByPlayer_StatusAndPayment_StatusAndPayment_StartDateLessThanEqual(PlayerStatus playerStatus, PaymentStatus paymentStatus, LocalDate date);

    Page<PlayerPaymentAssignmentJpaEntity> findAllByPlayer_Id(UUID playerId, Pageable pageable);
}
