package com.manager.payments.application.port.in;

import com.manager.payments.model.users.User;

import java.util.UUID;

public interface AssignPaymentToUserUseCase {

    public User assignPaymentToUser(UUID userId, UUID paymentId);

}
