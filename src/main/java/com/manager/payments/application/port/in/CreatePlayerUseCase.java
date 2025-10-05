package com.manager.payments.application.port.in;

import com.manager.payments.adapter.in.rest.dto.request.CreatePlayerRequestDTO;
import com.manager.payments.model.players.Player;

public interface CreatePlayerUseCase {
    Player createPlayer(CreatePlayerRequestDTO requestDTO);
}
