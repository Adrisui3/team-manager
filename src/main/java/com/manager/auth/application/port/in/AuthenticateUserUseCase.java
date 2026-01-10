package com.manager.auth.application.port.in;

import com.manager.auth.adapter.in.rest.dto.models.LoginResponseDto;
import com.manager.auth.adapter.in.rest.dto.requests.LoginUserRequestDto;

public interface AuthenticateUserUseCase {

    LoginResponseDto authenticate(LoginUserRequestDto loginUserRequestDto);

}
