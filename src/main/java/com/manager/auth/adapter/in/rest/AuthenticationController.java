package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.LoginResponseDto;
import com.manager.auth.adapter.dto.LoginUserDto;
import com.manager.auth.adapter.dto.RegisterUserDto;
import com.manager.auth.adapter.dto.SetUserPasswordDto;
import com.manager.auth.adapter.out.persistence.users.UserJpaEntity;
import com.manager.auth.application.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserJpaEntity> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        UserJpaEntity registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        LoginResponseDto loginResponseDto = authenticationService.authenticate(loginUserDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody SetUserPasswordDto setUserPasswordDto) {
        try {
            authenticationService.setPassword(setUserPasswordDto);
            return ResponseEntity.ok("Account verified.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
