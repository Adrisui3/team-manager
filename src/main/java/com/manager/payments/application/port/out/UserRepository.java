package com.manager.payments.application.port.out;

import com.manager.payments.model.users.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByPersonalId(String personalId);

}
