package com.manager.auth.application.service;

import com.manager.auth.adapter.dto.LoginResponseDto;
import com.manager.auth.adapter.dto.LoginUserDto;
import com.manager.auth.application.port.in.AuthenticateUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.InvalidEmailOrPasswordException;
import com.manager.auth.model.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDto authenticate(LoginUserDto loginUserDto) {
        User user =
                userRepository.findByEmail(loginUserDto.email()).orElseThrow(InvalidEmailOrPasswordException::new);
        user.authenticate(loginUserDto.password(), passwordEncoder);
        User savedUser = userRepository.save(user);
        return new LoginResponseDto(jwtService.generateToken(savedUser.getEmail()),
                jwtService.getJwtExpiration());
    }
}
