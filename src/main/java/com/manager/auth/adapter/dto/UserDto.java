package com.manager.auth.adapter.dto;

import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;

import java.util.UUID;

public record UserDto(UUID id, String email, String name, String surname, Role role,
                      boolean enabled) {

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName(), user.getSurname(),
                user.getRole(), user.isEnabled());
    }

}
