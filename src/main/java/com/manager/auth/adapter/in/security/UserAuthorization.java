package com.manager.auth.adapter.in.security;

import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("authz")
@RequiredArgsConstructor
public class UserAuthorization {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public boolean canUpdateUser(UUID targetUserId) {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();

        if (authenticatedUser.role() == Role.ADMIN)
            return true;

        return authenticatedUser.id().equals(targetUserId);
    }

    public boolean isCurrentUser(UUID targetUserId) {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        return authenticatedUser.id().equals(targetUserId);
    }
}
