package com.manager.auth.application.port.in;

import com.manager.auth.adapter.in.rest.dto.requests.RegisterUserRequestDto;
import com.manager.auth.model.users.User;

public interface SignUpUserUseCase {

    User signup(RegisterUserRequestDto registerUserRequestDto);

}
