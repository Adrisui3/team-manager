package com.manager.auth.application.port.out;

import com.manager.auth.model.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsById(UUID id);

    Page<User> findAll(String query, Pageable pageable);

    void deleteById(UUID id);
}
