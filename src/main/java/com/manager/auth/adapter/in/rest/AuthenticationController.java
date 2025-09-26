package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.*;
import com.manager.auth.application.port.in.AuthenticateUserUseCase;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.model.users.User;
import com.manager.shared.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final SignUpUserUseCase signUpUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthenticationController(SignUpUserUseCase signUpUserUseCase,
                                    AuthenticateUserUseCase authenticateUserUseCase) {
        this.signUpUserUseCase = signUpUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<UserDto>> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = signUpUserUseCase.signup(registerUserDto);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(),
                UserDto.from(registeredUser)));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponseDto>> authenticate(@RequestBody LoginUserDto loginUserDto) {
        LoginResponseDto loginResponseDto = authenticateUserUseCase.authenticate(loginUserDto);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(), loginResponseDto));
    }

    @PostMapping("/set-password")
    public ResponseEntity<ResponseDto<String>> setUserPassword(@RequestBody SetUserPasswordDto setUserPasswordDto) {
        signUpUserUseCase.setPassword(setUserPasswordDto);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(), "Password set " +
                "successfully,"));
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<String>> resetPassword(@RequestBody String email) {
        signUpUserUseCase.resetPassword(email);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(), "Verification code " +
                "resent."));
    }
}
