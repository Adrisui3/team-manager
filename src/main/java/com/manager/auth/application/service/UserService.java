package com.manager.auth.application.service;

import com.manager.auth.adapter.in.rest.dto.requests.UpdateUserRequestDto;
import com.manager.auth.application.port.in.UpdateUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UpdateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public User updateUser(UUID userId, UpdateUserRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));

        User updatedUser = user.toBuilder()
                .name(request.name())
                .surname(request.surname())
                .build();

        return userRepository.save(updatedUser);
    }
}
