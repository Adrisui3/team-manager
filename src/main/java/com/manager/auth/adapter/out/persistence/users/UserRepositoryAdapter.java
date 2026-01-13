package com.manager.auth.adapter.out.persistence.users;

import com.manager.auth.adapter.out.persistence.mapper.UserMapper;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository repository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = mapper.toUserJpaEntity(user);
        return mapper.toUser(repository.save(userJpaEntity));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(mapper::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<UserJpaEntity> users = repository.findAll(pageable);
        return users.map(mapper::toUser);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
