package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaRepository;
import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.users.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(PaymentJpaRepository paymentJpaRepository, UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = userMapper.toUserJpaEntity(user, paymentJpaRepository);
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

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserJpaEntity> userJpaEntity = userJpaRepository.findByEmail(email);
        return userJpaEntity.map(userMapper::toUser);
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }
}
