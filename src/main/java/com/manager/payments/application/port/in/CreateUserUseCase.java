package com.manager.payments.application.port.in;

import com.manager.payments.adapter.in.rest.dto.CreateUserRequestDTO;
import com.manager.payments.model.users.User;

public interface CreateUserUseCase {
    User createUser(CreateUserRequestDTO requestDTO);
}
