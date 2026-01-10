package com.manager.auth.application.port.in;

import com.manager.auth.adapter.in.rest.dto.requests.ChangeUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.RegisterUserRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.SetUserPasswordRequestDto;
import com.manager.auth.model.users.User;

public interface SignUpUserUseCase {

    User signup(RegisterUserRequestDto registerUserRequestDto);

    void setPassword(SetUserPasswordRequestDto setUserPasswordRequestDto);

    void resetPassword(String email);

    void changePassword(ChangeUserPasswordRequestDto changeUserPasswordRequestDto, User authenticatedUser);

}
