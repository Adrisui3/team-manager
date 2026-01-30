package com.manager.auth.application.service;

import com.manager.auth.adapter.in.rest.dto.requests.RegisterUserRequestDto;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.application.port.out.EmailService;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.UserAlreadyExists;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUserUseCase {

    private final UserRepository repository;
    private final EmailService emailService;

    @Override
    public User signup(RegisterUserRequestDto request) {
        if (repository.existsByEmail(request.email()))
            throw new UserAlreadyExists(request.email());

        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .surname(request.surname())
                .role(request.role())
                .enabled(false)
                .build()
                .initializeVerification();

        emailService.sendVerificationEmail(user.email(), user.verification().verificationCode());
        return repository.save(user);
    }
}
