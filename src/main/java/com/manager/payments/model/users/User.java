package com.manager.payments.model.users;

import java.time.LocalDate;
import java.util.UUID;

public record User(UUID id, String personalId, String name, String surname, String email, LocalDate birthDate,
                   Category category, UserStatus status) {
}
