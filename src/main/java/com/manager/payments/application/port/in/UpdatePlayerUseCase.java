package com.manager.payments.application.port.in;

import com.manager.payments.adapter.in.rest.dto.request.UpdatePlayerRequestDTO;
import com.manager.payments.model.players.Player;

import java.util.UUID;

public interface UpdatePlayerUseCase {
    Player updatePlayer(UUID playerId, UpdatePlayerRequestDTO request);
}
