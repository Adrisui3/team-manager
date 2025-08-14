package com.manager.payments.application.port.in;

import java.util.UUID;

public interface DeleteUserUseCase {
    void deleteUserById(UUID id);
}
