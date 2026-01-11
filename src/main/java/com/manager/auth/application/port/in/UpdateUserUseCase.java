package com.manager.auth.application.port.in;

import com.manager.auth.adapter.in.rest.dto.requests.ChangeUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.SetUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.UpdateUserRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.UpdateUserStatusDto;
import com.manager.auth.model.users.User;

import java.util.UUID;

public interface UpdateUserUseCase {

    User updateUser(UUID userId, UpdateUserRequestDto request);

    User updateUserStatus(UUID userId, UpdateUserStatusDto request);

    void setPassword(SetUserPasswordRequestDto setUserPasswordRequestDto);

    void resetPassword(UUID userId);

    void changePassword(UUID userId, ChangeUserPasswordRequestDto request);
}
