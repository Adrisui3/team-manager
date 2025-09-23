package com.manager.auth.application.port.in;

import com.manager.auth.adapter.dto.RegisterUserDto;
import com.manager.auth.adapter.dto.SetUserPasswordDto;
import com.manager.auth.model.users.User;

public interface SignUpUserUseCase {

    User signup(RegisterUserDto registerUserDto);

    void setPassword(SetUserPasswordDto setUserPasswordDto);

    void resetPassword(String email);

}
