package com.manager.auth.application.port.in;

import java.util.UUID;

public interface DeleteUserUseCase {
    void deleteUser(UUID userId);
}
