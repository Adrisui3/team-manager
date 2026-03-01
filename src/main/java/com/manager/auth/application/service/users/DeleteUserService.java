package com.manager.auth.application.service.users;

import com.manager.auth.application.port.in.DeleteUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.UserNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUserService implements DeleteUserUseCase {

    private final UserRepository repository;

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        if (!repository.existsById(userId)) {
            throw UserNotFound.byId(userId);
        }

        repository.deleteById(userId);
    }
}
