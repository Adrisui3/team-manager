package com.manager.auth.application.port.in;

import com.manager.auth.model.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FindUserUseCase {

    Page<User> findAll(String query, Pageable pageable);

    User findById(UUID id);
}
