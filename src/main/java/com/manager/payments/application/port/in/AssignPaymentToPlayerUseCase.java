package com.manager.payments.application.port.in;

import com.manager.payments.model.players.Player;

import java.util.UUID;

public interface AssignPaymentToPlayerUseCase {

    Player assignPaymentToPlayer(UUID playerId, UUID paymentId);

}
