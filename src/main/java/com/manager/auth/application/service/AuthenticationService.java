package com.manager.auth.application.service;

import com.manager.auth.adapter.dto.models.LoginResponseDto;
import com.manager.auth.adapter.dto.requests.LoginUserRequestDto;
import com.manager.auth.application.port.in.AuthenticateUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.InvalidEmailOrPasswordException;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto authenticate(LoginUserRequestDto loginUserRequestDto) {
        User user =
                userRepository.findByEmail(loginUserRequestDto.email()).orElseThrow(InvalidEmailOrPasswordException::new);
        User authenticatedUser = user.authenticate(loginUserRequestDto.password(), passwordEncoder);
        User savedUser = userRepository.save(authenticatedUser);
        return new LoginResponseDto(jwtService.generateToken(savedUser.email()),
                jwtService.getJwtExpiration());
    }
}
