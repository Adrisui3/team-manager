package com.manager.auth.adapter.in.security;

import com.manager.auth.model.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        return securityUserDetails.getUser();
    }
}
