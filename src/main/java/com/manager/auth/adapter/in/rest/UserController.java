package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.in.security.AuthenticatedUserProvider;
import com.manager.auth.model.users.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public UserController(AuthenticatedUserProvider authenticatedUserProvider) {
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(authenticatedUserProvider.getAuthenticatedUser());
    }
}
