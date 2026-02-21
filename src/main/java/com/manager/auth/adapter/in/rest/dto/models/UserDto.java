package com.manager.auth.adapter.in.rest.dto.models;

import com.manager.auth.model.roles.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(UUID id, String email, String name, String surname, Role role, boolean enabled,
                      LocalDateTime lastLogIn) {

}
