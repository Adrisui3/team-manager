package com.manager.auth.application.service;

import com.manager.auth.adapter.in.rest.dto.requests.ChangeUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.SetUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.UpdateUserRequestDto;
import com.manager.auth.application.port.in.UpdateUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.DisabledUserException;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UpdateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public User updateUser(UUID userId, UpdateUserRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));

        User updatedUser = user.toBuilder()
                .name(request.name())
                .surname(request.surname())
                .build();

        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void setPassword(SetUserPasswordRequestDto setUserPasswordRequestDto) {
        User user =
                userRepository.findByEmail(setUserPasswordRequestDto.email()).orElseThrow(() -> UserNotFound.byEmail(setUserPasswordRequestDto.email()));
        LocalDateTime now = LocalDateTime.now();
        User updatedUser = user.setPassword(setUserPasswordRequestDto.verificationCode(),
                setUserPasswordRequestDto.password(), passwordEncoder, now);

        userRepository.save(updatedUser);
    }

    @Override
    public void resetPassword(String email) {
        User user =
                userRepository.findByEmail(email).orElseThrow(() -> UserNotFound.byEmail(email));
        if (!user.enabled())
            throw new DisabledUserException();

        User updatedUser = user.initializeVerification();

        emailService.sendInvitationEmail(updatedUser.email(), updatedUser.verification().verificationCode());
        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(UUID userId, ChangeUserPasswordRequestDto changeUserPasswordRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));

        User updatedUser = user.changePassword(changeUserPasswordRequestDto.email(),
                changeUserPasswordRequestDto.oldPassword(), changeUserPasswordRequestDto.newPassword(),
                passwordEncoder);
        userRepository.save(updatedUser);
    }
}
