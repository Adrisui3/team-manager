package com.manager.auth.application.port.in;

import com.manager.auth.adapter.dto.LoginResponseDto;
import com.manager.auth.adapter.dto.LoginUserDto;

public interface AuthenticateUserUseCase {

    LoginResponseDto authenticate(LoginUserDto loginUserDto);

}
