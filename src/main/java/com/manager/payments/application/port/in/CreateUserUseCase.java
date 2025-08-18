package com.manager.payments.application.port.in;

import com.manager.payments.adapter.in.rest.dto.CreatePlayerRequestDTO;
import com.manager.payments.model.users.Player;

public interface CreateUserUseCase {
    Player createUser(CreatePlayerRequestDTO requestDTO);
}
