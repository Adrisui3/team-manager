package com.manager.payments.application.port.in;

import com.manager.payments.model.users.User;

import java.util.UUID;

public interface FindUserUseCase {
    User findById(UUID id);

    User findByEmail(String email);
}
