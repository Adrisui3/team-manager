package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaEntity;
import com.manager.payments.adapter.out.persistence.players.PlayerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerPaymentAssignmentJpaRepository extends JpaRepository<PlayerPaymentAssignmentJpaEntity, UUID> {

    Optional<PlayerPaymentAssignmentJpaEntity> findByPlayerAndPayment(PlayerJpaEntity player, PaymentJpaEntity payment);

    List<PlayerPaymentAssignmentJpaEntity> findAllByPlayer(PlayerJpaEntity player);

    List<PlayerPaymentAssignmentJpaEntity> findAllByPayment(PaymentJpaEntity payment);

    boolean existsByPlayerAndPayment(PlayerJpaEntity player, PaymentJpaEntity payment);

    List<PlayerPaymentAssignmentJpaEntity> findAllByActiveIsTrueAndPaymentStartDateLessThanEqual(LocalDate date);

}
