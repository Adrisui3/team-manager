package com.manager.auth.application.service;

import com.manager.auth.adapter.in.rest.dto.requests.RegisterUserRequestDto;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.UserAlreadyExists;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUserUseCase {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public User signup(RegisterUserRequestDto registerUserRequestDto) {
        if (userRepository.existsByEmail(registerUserRequestDto.email()))
            throw new UserAlreadyExists(registerUserRequestDto.email());

        User user = User.builder()
                .email(registerUserRequestDto.email())
                .name(registerUserRequestDto.name())
                .surname(registerUserRequestDto.surname())
                .role(registerUserRequestDto.role())
                .enabled(false)
                .build()
                .initializeVerification();

        emailService.sendInvitationEmail(user.email(), user.verification().verificationCode());
        return userRepository.save(user);
    }
}
