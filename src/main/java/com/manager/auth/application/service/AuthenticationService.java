package com.manager.auth.application.service;

import com.manager.auth.adapter.dto.LoginResponseDto;
import com.manager.auth.adapter.dto.LoginUserDto;
import com.manager.auth.adapter.dto.RegisterUserDto;
import com.manager.auth.adapter.dto.SetUserPasswordDto;
import com.manager.auth.adapter.out.persistence.users.UserJpaEntity;
import com.manager.auth.adapter.out.persistence.users.UserJpaRepository;
import com.manager.auth.adapter.out.persistence.users.UserVerificationJpaEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    public final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(JwtService jwtService, UserJpaRepository userJpaRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, EmailService emailService) {
        this.jwtService = jwtService;
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public UserJpaEntity signup(RegisterUserDto registerUserDto) {
        UserJpaEntity user = new UserJpaEntity();
        user.setEmail(registerUserDto.email());
        user.setName(registerUserDto.name());
        user.setSurname(registerUserDto.surname());
        user.setEnabled(false);

        UserVerificationJpaEntity userVerificationJpaEntity = new UserVerificationJpaEntity();
        userVerificationJpaEntity.setVerificationCode(generateVerificationCode());
        userVerificationJpaEntity.setExpirationDate(LocalDateTime.now().plusMinutes(15));

        user.setVerification(userVerificationJpaEntity);
        userVerificationJpaEntity.setUser(user);

        emailService.sendInvitationEmail(user.getEmail(), user.getVerification().getVerificationCode());
        return userJpaRepository.save(user);
    }

    public void setPassword(SetUserPasswordDto setUserPasswordDto) {
        Optional<UserJpaEntity> optionalUser = userJpaRepository.findByEmail(setUserPasswordDto.email());
        if (optionalUser.isPresent()) {
            UserJpaEntity userJpaEntity = optionalUser.get();
            UserVerificationJpaEntity userVerificationJpaEntity = userJpaEntity.getVerification();
            if (userVerificationJpaEntity == null) {
                throw new RuntimeException("No verification exists for user.");
            }

            if (userVerificationJpaEntity.getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code expired.");
            }

            if (userVerificationJpaEntity.getVerificationCode().equals(setUserPasswordDto.verificationCode())) {
                userJpaEntity.setEnabled(true);
                userJpaEntity.setPassword(passwordEncoder.encode(setUserPasswordDto.password()));
                userJpaEntity.setVerification(null);
                userJpaRepository.save(userJpaEntity);
            } else {
                throw new RuntimeException("Invalid verification code.");
            }
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    public LoginResponseDto authenticate(LoginUserDto loginUserDto) {
        UserJpaEntity userJpaEntity =
                userJpaRepository.findByEmail(loginUserDto.email()).orElseThrow(() -> new RuntimeException("Invalid " +
                        "email or password"));

        if (!userJpaEntity.isEnabled()) {
            throw new RuntimeException("Disabled account");
        }

        if (!passwordEncoder.matches(loginUserDto.password(), userJpaEntity.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        userJpaEntity.setLastLogIn(LocalDateTime.now());
        UserJpaEntity savedUser = userJpaRepository.save(userJpaEntity);
        return new LoginResponseDto(jwtService.generateToken(savedUser), jwtService.getJwtExpiration());
    }

    public void resendVerificationCode(String email) {
        Optional<UserJpaEntity> userJpaEntity = userJpaRepository.findByEmail(email);
        if (userJpaEntity.isPresent()) {
            UserJpaEntity user = userJpaEntity.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account already verified.");
            }

            UserVerificationJpaEntity userVerificationJpaEntity = new UserVerificationJpaEntity();
            userVerificationJpaEntity.setVerificationCode(generateVerificationCode());
            userVerificationJpaEntity.setExpirationDate(LocalDateTime.now().plusMinutes(15));

            user.setVerification(userVerificationJpaEntity);
            userVerificationJpaEntity.setUser(user);

            emailService.sendInvitationEmail(user.getEmail(), user.getVerification().getVerificationCode());
            userJpaRepository.save(user);
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    public UserJpaEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserJpaEntity) authentication.getPrincipal();
    }


    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999) + 10000;
        return String.valueOf(code);
    }
}
