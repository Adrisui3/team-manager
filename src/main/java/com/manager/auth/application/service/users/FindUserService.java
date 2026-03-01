package com.manager.auth.application.service.users;

import com.manager.auth.application.port.in.FindUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindUserService implements FindUserUseCase {

    private final UserRepository repository;

    @Override
    public Page<User> findAll(String query, Pageable pageable) {
        return repository.findAll(query.trim().toLowerCase(Locale.ROOT), pageable);
    }

    @Override
    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> UserNotFound.byId(id));
    }
}
