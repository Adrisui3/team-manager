package com.manager.auth.application.port.in;

import java.util.UUID;

public interface LinkPlayerToUserUseCase {

    void linkPlayerToUser(UUID userId, UUID playerId);

    void unlinkPlayerToUser(UUID userId);

}
