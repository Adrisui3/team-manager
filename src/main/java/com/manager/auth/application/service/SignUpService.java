package com.manager.auth.application.service;

import com.manager.auth.adapter.dto.RegisterUserDto;
import com.manager.auth.adapter.dto.SetUserPasswordDto;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.users.User;
import com.manager.auth.model.users.UserVerification;
import com.manager.auth.model.users.UserVerificationFactory;
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
    public User signup(RegisterUserDto registerUserDto) {
        if (userRepository.existsByEmail(registerUserDto.email()))
            throw new RuntimeException("Email already in use");

        User user = new User();
        user.setEmail(registerUserDto.email());
        user.setName(registerUserDto.name());
        user.setSurname(registerUserDto.surname());
        user.setRole(registerUserDto.role());
        user.setEnabled(false);

        UserVerification userVerification = UserVerificationFactory.build(user);
        user.setVerification(userVerification);

        emailService.sendInvitationEmail(user.getEmail(), user.getVerification().getVerificationCode());
        return userRepository.save(user);
    }

    @Override
    public void setPassword(SetUserPasswordDto setUserPasswordDto) {
        User user =
                userRepository.findByEmail(setUserPasswordDto.email()).orElseThrow(() -> new RuntimeException(
                        "User not found."));
        UserVerification userVerification = user.getVerification();
        if (userVerification == null) {
            throw new RuntimeException("No verification exists for user.");
        }

        if (userVerification.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code expired.");
        }

        if (userVerification.getVerificationCode().equals(setUserPasswordDto.verificationCode())) {
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(setUserPasswordDto.password()));
            user.setVerification(null);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid verification code.");
        }
    }

    @Override
    public void resetPassword(String email) {
        User user =
                userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found."));
        UserVerification userVerification = UserVerificationFactory.build(user);
        user.setVerification(userVerification);

        emailService.sendInvitationEmail(user.getEmail(), user.getVerification().getVerificationCode());
        userRepository.save(user);
    }
}
