package com.manager.auth.application.service;

import com.manager.auth.adapter.dto.requests.ChangeUserPasswordRequestDto;
import com.manager.auth.adapter.dto.requests.RegisterUserRequestDto;
import com.manager.auth.adapter.dto.requests.SetUserPasswordRequestDto;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.UserAlreadyExists;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SignUpService implements SignUpUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public SignUpService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public User signup(RegisterUserRequestDto registerUserRequestDto) {
        if (userRepository.existsByEmail(registerUserRequestDto.email()))
            throw new UserAlreadyExists(registerUserRequestDto.email());

        User user = User.builder()
                .email(registerUserRequestDto.email())
                .name(registerUserRequestDto.name())
                .surname(registerUserRequestDto.surname())
                .role(registerUserRequestDto.role())
                .enabled(true)
                .build()
                .initializeVerification();

        emailService.sendInvitationEmail(user.email(), user.verification().verificationCode());
        return userRepository.save(user);
    }

    @Override
    public void setPassword(SetUserPasswordRequestDto setUserPasswordRequestDto) {
        User user = userRepository.findByEmail(setUserPasswordRequestDto.email()).orElseThrow(UserNotFound::new);
        LocalDateTime now = LocalDateTime.now();
        User updatedUser = user.setPassword(setUserPasswordRequestDto.verificationCode(),
                setUserPasswordRequestDto.password(), passwordEncoder, now);

        userRepository.save(updatedUser);
    }

    @Override
    public void resetPassword(String email) {
        User user =
                userRepository.findByEmail(email).orElseThrow(UserNotFound::new);
        User updatedUser = user.initializeVerification();

        emailService.sendInvitationEmail(updatedUser.email(), updatedUser.verification().verificationCode());
        userRepository.save(updatedUser);
    }

    @Override
    public void changePassword(ChangeUserPasswordRequestDto changeUserPasswordRequestDto, User authenticatedUser) {
        authenticatedUser.changePassword(changeUserPasswordRequestDto.email(),
                changeUserPasswordRequestDto.oldPassword(),
                changeUserPasswordRequestDto.newPassword(), passwordEncoder);
        userRepository.save(authenticatedUser);
    }
}
