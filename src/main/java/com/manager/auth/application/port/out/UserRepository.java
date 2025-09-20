package com.manager.auth.application.port.out;

import com.manager.auth.model.users.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

}
