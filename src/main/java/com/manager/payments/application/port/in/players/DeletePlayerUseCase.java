package com.manager.payments.application.port.in.players;

import java.util.UUID;

public interface DeletePlayerUseCase {

    void deleteById(UUID playerId);
}
