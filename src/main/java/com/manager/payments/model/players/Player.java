package com.manager.payments.model.players;

import java.time.LocalDate;
import java.util.UUID;

public record Player(UUID id, String personalId, String name, String surname, String email, LocalDate birthDate,
                     Category category, PlayerStatus status) {

    public Player(String personalId, String name, String surname, String email, LocalDate birthDate,
                  Category category, PlayerStatus status) {
        this(null, personalId, name, surname, email, birthDate, category, status);
    }
}
