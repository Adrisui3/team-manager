package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return null;
    }
}
