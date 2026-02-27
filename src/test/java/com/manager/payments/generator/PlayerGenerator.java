package com.manager.payments.generator;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class PlayerGenerator {

    private final Player.PlayerBuilder builder;

    private PlayerGenerator() {
        this.builder = Player.builder();
    }

    public static PlayerGenerator player() {
        PlayerGenerator generator = new PlayerGenerator();
        generator.id(UUID.randomUUID());
        generator.personalId("12345678");
        generator.name("John");
        generator.surname("Doe");
        generator.email("player@test.com");
        generator.birthDate(LocalDate.of(2000, 1, 1));
        generator.category(Category.SENIOR);
        generator.status(PlayerStatus.ENABLED);
        generator.gender(PlayerGender.MASCULINO);
        generator.phoneNumber("999999999");
        generator.createdAt(LocalDateTime.now());
        generator.updatedAt(LocalDateTime.now());
        return generator;
    }

    public PlayerGenerator id(UUID id) {
        builder.id(id);
        return this;
    }

    public PlayerGenerator personalId(String personalId) {
        builder.personalId(personalId);
        return this;
    }

    public PlayerGenerator name(String name) {
        builder.name(name);
        return this;
    }

    public PlayerGenerator surname(String surname) {
        builder.surname(surname);
        return this;
    }

    public PlayerGenerator email(String email) {
        builder.email(email);
        return this;
    }

    public PlayerGenerator secondaryEmail(String secondaryEmail) {
        builder.secondaryEmail(secondaryEmail);
        return this;
    }

    public PlayerGenerator birthDate(LocalDate birthDate) {
        builder.birthDate(birthDate);
        return this;
    }

    public PlayerGenerator category(Category category) {
        builder.category(category);
        return this;
    }

    public PlayerGenerator status(PlayerStatus status) {
        builder.status(status);
        return this;
    }

    public PlayerGenerator gender(PlayerGender gender) {
        builder.gender(gender);
        return this;
    }

    public PlayerGenerator phoneNumber(String phoneNumber) {
        builder.phoneNumber(phoneNumber);
        return this;
    }

    public PlayerGenerator secondaryPhoneNumber(String secondaryPhoneNumber) {
        builder.secondaryPhoneNumber(secondaryPhoneNumber);
        return this;
    }

    public PlayerGenerator updatedAt(LocalDateTime updatedAt) {
        builder.updatedAt(updatedAt);
        return this;
    }

    public PlayerGenerator createdAt(LocalDateTime createdAt) {
        builder.createdAt(createdAt);
        return this;
    }

    public Player build() {
        return builder.build();
    }
}
