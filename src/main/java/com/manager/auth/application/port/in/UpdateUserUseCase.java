package com.manager.auth.application.port.in;

import com.manager.auth.adapter.in.rest.dto.requests.UpdateUserRequestDto;
import com.manager.auth.model.users.User;

import java.util.UUID;

public interface UpdateUserUseCase {

    User updateUser(UUID userId, UpdateUserRequestDto request);
}
