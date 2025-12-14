package com.manager.auth.application.service;

import com.manager.auth.adapter.dto.ChangeUserPasswordDto;
import com.manager.auth.adapter.dto.RegisterUserDto;
import com.manager.auth.adapter.dto.SetUserPasswordDto;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.UserAlreadyExists;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new UserAlreadyExists(registerUserDto.email());

        User user = new User();
        user.setEmail(registerUserDto.email());
        user.setName(registerUserDto.name());
        user.setSurname(registerUserDto.surname());
        user.setRole(registerUserDto.role());
        user.setEnabled(false);
        user.setVerification();

        emailService.sendInvitationEmail(user.getEmail(), user.getVerification().getVerificationCode());
        return userRepository.save(user);
    }

    @Override
    public void setPassword(SetUserPasswordDto setUserPasswordDto) {
        User user = userRepository.findByEmail(setUserPasswordDto.email()).orElseThrow(UserNotFound::new);
        user.setPassword(setUserPasswordDto.verificationCode(), setUserPasswordDto.password(), passwordEncoder);

        userRepository.save(user);
    }

    @Override
    public void resetPassword(String email) {
        User user =
                userRepository.findByEmail(email).orElseThrow(UserNotFound::new);
        user.setVerification();

        emailService.sendInvitationEmail(user.getEmail(), user.getVerification().getVerificationCode());
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangeUserPasswordDto changeUserPasswordDto, User authenticatedUser) {
        authenticatedUser.changePassword(changeUserPasswordDto.email(), changeUserPasswordDto.oldPassword(),
                changeUserPasswordDto.newPassword(), passwordEncoder);
        userRepository.save(authenticatedUser);
    }
}
