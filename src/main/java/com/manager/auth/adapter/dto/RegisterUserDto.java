package com.manager.auth.adapter.dto;

import com.manager.auth.model.roles.Role;

public record RegisterUserDto(String email, String name, String surname, Role role) {

}
