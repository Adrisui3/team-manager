package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.users.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

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
        UserJpaEntity savedUserJpaEntity = userJpaRepository.save(userJpaEntity);
        return userMapper.toUser(savedUserJpaEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserJpaEntity> userJpaEntity = userJpaRepository.findById(id);
        return userJpaEntity.map(userMapper::toUser);
    }

    @Override
    public Optional<User> findByPersonalId(String personalId) {
        Optional<UserJpaEntity> userJpaEntity = userJpaRepository.findByPersonalId(personalId);
        return userJpaEntity.map(userMapper::toUser);
    }
}
