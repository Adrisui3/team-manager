package com.manager.auth.application.port.in;

import com.manager.auth.adapter.dto.models.LoginResponseDto;
import com.manager.auth.adapter.dto.requests.LoginUserRequestDto;

public interface AuthenticateUserUseCase {

    LoginResponseDto authenticate(LoginUserRequestDto loginUserRequestDto);

}
