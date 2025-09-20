package com.manager.auth.application.service;

import com.manager.auth.adapter.dto.LoginResponseDto;
import com.manager.auth.adapter.dto.LoginUserDto;
import com.manager.auth.adapter.dto.RegisterUserDto;
import com.manager.auth.adapter.dto.SetUserPasswordDto;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.users.User;
import com.manager.auth.model.users.UserVerification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService,
                                 PasswordEncoder passwordEncoder,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User signup(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setEmail(registerUserDto.email());
        user.setName(registerUserDto.name());
        user.setSurname(registerUserDto.surname());
        user.setEnabled(false);

        UserVerification userVerification = new UserVerification();
        userVerification.setVerificationCode(generateVerificationCode());
        userVerification.setExpirationDate(LocalDateTime.now().plusMinutes(15));

        user.setVerification(userVerification);
        userVerification.setUserId(user.getId());

        emailService.sendInvitationEmail(user.getEmail(), user.getVerification().getVerificationCode());
        return userRepository.save(user);
    }

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

    public LoginResponseDto authenticate(LoginUserDto loginUserDto) {
        User user =
                userRepository.findByEmail(loginUserDto.email()).orElseThrow(() -> new RuntimeException("Invalid " +
                        "email or password"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Disabled account");
        }

        if (!passwordEncoder.matches(loginUserDto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        user.setLastLogIn(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return new LoginResponseDto(jwtService.generateToken(savedUser.getEmail()),
                jwtService.getJwtExpiration());
    }

    public void resendVerificationCode(String email) {
        User user =
                userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found."));
        if (user.isEnabled()) {
            throw new RuntimeException("Account already verified.");
        }

        UserVerification userVerification = new UserVerification();
        userVerification.setVerificationCode(generateVerificationCode());
        userVerification.setExpirationDate(LocalDateTime.now().plusMinutes(15));

        user.setVerification(userVerification);
        userVerification.setUserId(user.getId());

        emailService.sendInvitationEmail(user.getEmail(), user.getVerification().getVerificationCode());
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999) + 10000;
        return String.valueOf(code);
    }
}
