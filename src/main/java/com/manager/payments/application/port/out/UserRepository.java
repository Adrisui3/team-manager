package com.manager.payments.application.port.out;

import com.manager.payments.model.users.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> getById(UUID id);

    Optional<User> getByPersonalId(String personalId);

}
