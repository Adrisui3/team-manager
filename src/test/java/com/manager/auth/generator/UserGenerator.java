package com.manager.auth.generator;

import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;
import com.manager.auth.model.users.UserVerification;
import com.manager.payments.model.players.Player;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class UserGenerator {

    private final User.UserBuilder builder;

    private UserGenerator() {
        this.builder = User.builder();
    }

    public static UserGenerator user() {
        UserGenerator generator = new UserGenerator();
        generator.id(UUID.randomUUID());
        generator.email("user@test.com");
        generator.name("John");
        generator.surname("Doe");
        generator.role(Role.ADMIN);
        generator.enabled(true);
        generator.createdAt(LocalDateTime.now());
        generator.updatedAt(LocalDateTime.now());
        return generator;
    }

    public UserGenerator id(UUID id) {
        builder.id(id);
        return this;
    }

    public UserGenerator email(String email) {
        builder.email(email);
        return this;
    }

    public UserGenerator password(String password) {
        builder.password(password);
        return this;
    }

    public UserGenerator name(String name) {
        builder.name(name);
        return this;
    }

    public UserGenerator surname(String surname) {
        builder.surname(surname);
        return this;
    }

    public UserGenerator lastLogIn(LocalDateTime lastLogIn) {
        builder.lastLogIn(lastLogIn);
        return this;
    }

    public UserGenerator role(Role role) {
        builder.role(role);
        return this;
    }

    public UserGenerator enabled(boolean enabled) {
        builder.enabled(enabled);
        return this;
    }

    public UserGenerator verification(UserVerification verification) {
        builder.verification(verification);
        return this;
    }

    public UserGenerator createdAt(LocalDateTime createdAt) {
        builder.createdAt(createdAt);
        return this;
    }

    public UserGenerator updatedAt(LocalDateTime updatedAt) {
        builder.updatedAt(updatedAt);
        return this;
    }

    public UserGenerator players(List<Player> players) {
        builder.players(players);
        return this;
    }

    public User build() {
        return builder.build();
    }
}
