package com.manager.payments.application.port.out;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerPaymentAssignmentRepository {

    PlayerPaymentAssignment save(PlayerPaymentAssignment playerPaymentAssignment);

    Optional<PlayerPaymentAssignment> findById(UUID id);

    boolean existsByPlayerIdAndPaymentId(UUID playerId, UUID paymentId);

    List<PlayerPaymentAssignment> findAllActiveAndStartDateBeforeOrEqual(LocalDate date);

    void deleteByPlayerIdAndPaymentId(UUID playerId, UUID paymentId);
}
