package com.manager.payments.application.port.in.players;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;

import java.util.UUID;

public interface AssignPaymentToPlayerUseCase {

    PlayerPaymentAssignment assignPaymentToPlayer(UUID playerId, UUID paymentId);

    void unassignPaymentToPlayer(UUID playerId, UUID paymentId);
}
