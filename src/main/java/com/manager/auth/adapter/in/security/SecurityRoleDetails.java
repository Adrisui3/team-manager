package com.manager.auth.adapter.in.security;

import com.manager.auth.model.roles.Role;
import org.springframework.security.core.GrantedAuthority;

public record SecurityRoleDetails(Role role) implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return "ROLE_" + role.name();
    }
}
