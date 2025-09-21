package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.LoginResponseDto;
import com.manager.auth.adapter.dto.LoginUserDto;
import com.manager.auth.adapter.dto.RegisterUserDto;
import com.manager.auth.adapter.dto.SetUserPasswordDto;
import com.manager.auth.application.port.in.AuthenticateUserUseCase;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.model.users.User;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = signUpUserUseCase.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
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
            return ResponseEntity.ok("Account verified.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody String email) {
        try {
            signUpUserUseCase.resetPassword(email);
            return ResponseEntity.ok("Verification code resent.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
