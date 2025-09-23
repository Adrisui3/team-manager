package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.*;
import com.manager.auth.application.port.in.AuthenticateUserUseCase;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.model.users.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        try {
            User registeredUser = signUpUserUseCase.signup(registerUserDto);
            return ResponseEntity.ok(RegisteredUserResponseDto.from(registeredUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
        LoginResponseDto loginResponseDto = authenticateUserUseCase.authenticate(loginUserDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/set-password")
    public ResponseEntity<?> setUserPassword(@RequestBody SetUserPasswordDto setUserPasswordDto) {
        try {
            signUpUserUseCase.setPassword(setUserPasswordDto);
            return ResponseEntity.ok("Password set successfully,");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@RequestBody String email) {
        try {
            signUpUserUseCase.resetPassword(email);
            return ResponseEntity.ok("Verification code resent.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
