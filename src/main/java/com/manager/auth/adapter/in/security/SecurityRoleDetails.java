package com.manager.auth.adapter.in.security;

import com.manager.auth.model.roles.Role;
import org.springframework.security.core.GrantedAuthority;

public class SecurityRoleDetails implements GrantedAuthority {

    private final Role role;

    public SecurityRoleDetails(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + role.name();
    }
}
