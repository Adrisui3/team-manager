package com.manager.auth.adapter.dto.models;

import com.manager.auth.model.roles.Role;

import java.util.UUID;

public record UserDto(UUID id, String email, String name, String surname, Role role, boolean enabled) {

}
