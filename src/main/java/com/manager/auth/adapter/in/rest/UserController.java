package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.out.persistence.users.UserJpaEntity;
import com.manager.auth.application.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserJpaEntity> getCurrentUser() {
        return ResponseEntity.ok(authenticationService.getAuthenticatedUser());
    }
}
