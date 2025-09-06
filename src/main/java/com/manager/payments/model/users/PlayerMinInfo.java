package com.manager.payments.model.users;

import java.time.LocalDate;
import java.util.UUID;

public record PlayerMinInfo(UUID id, String personalId, String name, String surname, String email, LocalDate birthDate,
                            Category category, PlayerStatus status) {

    public static PlayerMinInfo from(Player player) {
        return new PlayerMinInfo(player.id(), player.personalId(), player.name(), player.surname(), player.email(),
                player.birthDate(), player.category(), player.status());
    }

}
