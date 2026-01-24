package com.manager.auth.application.service;

import com.manager.auth.adapter.in.rest.dto.requests.ChangeUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.SetUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.UpdateUserRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.UpdateUserStatusDto;
import com.manager.auth.application.port.in.DeleteUserUseCase;
import com.manager.auth.application.port.in.FindUserUseCase;
import com.manager.auth.application.port.in.UpdateUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.application.service.email.EmailService;
import com.manager.auth.model.exceptions.DisabledUserException;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UpdateUserUseCase, DeleteUserUseCase, FindUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public User updateUser(UUID userId, UpdateUserRequestDto request) {
        User user = repository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));

        User updatedUser = user.toBuilder()
                .name(request.name())
                .surname(request.surname())
                .build();

        return repository.save(updatedUser);
    }

    @Override
    @Transactional
    public User updateUserStatus(UUID userId, UpdateUserStatusDto request) {
        User user = repository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));

        User updatedUser = user.toBuilder()
                .enabled(request.enabled())
                .build();

        return repository.save(updatedUser);
    }

    @Override
    @Transactional
    public void setPassword(SetUserPasswordRequestDto request) {
        User user =
                repository.findByEmail(request.email()).orElseThrow(() -> UserNotFound.byEmail(request.email()));
        LocalDateTime now = LocalDateTime.now();
        User updatedUser = user.setPassword(request.verificationCode(),
                request.password(), passwordEncoder, now);

        repository.save(updatedUser);
    }

    @Override
    public void resetPassword(UUID userId) {
        User user =
                repository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));
        if (!user.enabled())
            throw new DisabledUserException();

        User updatedUser = user.initializeVerification();

        emailService.sendVerificationEmail(updatedUser.email(), updatedUser.verification().verificationCode());
        repository.save(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(UUID userId, ChangeUserPasswordRequestDto request) {
        User user = repository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));
        User updatedUser = user.changePassword(request.oldPassword(), request.newPassword(), passwordEncoder);
        repository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        if (!repository.existsById(userId)) {
            throw UserNotFound.byId(userId);
        }

        repository.deleteById(userId);
    }

    @Override
    public Page<User> findAll(String query, Pageable pageable) {
        return repository.findAll(query.trim().toLowerCase(Locale.ROOT), pageable);
    }

    @Override
    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> UserNotFound.byId(id));
    }
}
