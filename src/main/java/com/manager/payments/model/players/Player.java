package com.manager.payments.model.players;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Player(UUID id, String personalId, String name, String surname, String email, String secondaryEmail,
                     LocalDate birthDate, Category category, PlayerStatus status, PlayerGender gender,
                     String phoneNumber, String secondaryPhoneNumber, LocalDateTime updatedAt,
                     LocalDateTime createdAt) {
}
