package com.manager.payments.model.users;

import java.time.LocalDate;
import java.util.UUID;

public record UserMinInfo(UUID id, String personalId, String name, String surname, String email, LocalDate birthDate,
                          Category category, UserStatus status) {

    public static UserMinInfo from(User user) {
        return new UserMinInfo(user.id(), user.personalId(), user.name(), user.surname(), user.email(), user.birthDate(), user.category(), user.status());
    }

}
