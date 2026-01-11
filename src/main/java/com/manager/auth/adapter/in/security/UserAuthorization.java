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

    public boolean canUpdateUserBasicData(UUID targetUserId) {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();

        if (authenticatedUser.role() == Role.ADMIN)
            return true;

        return isCurrentUser(targetUserId, authenticatedUser);
    }

    public boolean isCurrentUser(UUID targetUserId) {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        return isCurrentUser(targetUserId, authenticatedUser);
    }

    private boolean isCurrentUser(UUID targetUserId, User authenticatedUser) {
        return authenticatedUser.id().equals(targetUserId);
    }
}
