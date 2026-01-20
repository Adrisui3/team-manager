package com.manager.payments.model.players;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
public record Player(UUID id, String personalId, String name, String surname, String email, LocalDate birthDate,
                     Category category, PlayerStatus status, PlayerGender gender) {
}
