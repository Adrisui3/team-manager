package com.manager.payments.application.port.in;

import com.manager.payments.model.users.Player;

import java.util.UUID;

public interface AssignPaymentToUserUseCase {

    Player assignPaymentToPlayer(UUID userId, UUID paymentId);

}
