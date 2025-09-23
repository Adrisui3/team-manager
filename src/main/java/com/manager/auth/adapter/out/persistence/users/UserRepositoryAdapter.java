package com.manager.auth.adapter.out.persistence.users;

import com.manager.auth.adapter.out.persistence.mapper.UserMapper;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.users.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = userMapper.toUserJpaEntity(user);
        return userMapper.toUser(userJpaRepository.save(userJpaEntity));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userMapper::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
