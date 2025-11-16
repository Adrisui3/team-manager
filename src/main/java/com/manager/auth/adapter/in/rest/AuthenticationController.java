package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.*;
import com.manager.auth.adapter.out.persistence.mapper.UserMapper;
import com.manager.auth.application.port.in.AuthenticateUserUseCase;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.model.users.User;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management endpoints")
@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final SignUpUserUseCase signUpUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserMapper userMapper;

    public AuthenticationController(SignUpUserUseCase signUpUserUseCase,
                                    AuthenticateUserUseCase authenticateUserUseCase, UserMapper userMapper) {
        this.signUpUserUseCase = signUpUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Create a new user", description = "Only users with ADMIN role can perform this action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "User already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class)))
    })
    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<UserDto>> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = signUpUserUseCase.signup(registerUserDto);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), userMapper.toUserDto(registeredUser)));
    }

    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Invalid email or password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponseDto>> authenticate(@RequestBody LoginUserDto loginUserDto) {
        LoginResponseDto loginResponseDto = authenticateUserUseCase.authenticate(loginUserDto);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), loginResponseDto));
    }

    @Operation(summary = "Set user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's password successfully set",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User or verification request not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Verification code expired or invalid",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class)))
    })
    @PostMapping("/set-password")
    public ResponseEntity<ResponseDto<String>> setPassword(@RequestBody SetUserPasswordDto setUserPasswordDto) {
        signUpUserUseCase.setPassword(setUserPasswordDto);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "Password set successfully,"));
    }

    @Operation(summary = "Reset a user's password", description = "Only users with ADMIN role can perform this action.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's password successfully reset",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class)))
    })
    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<String>> resetPassword(@RequestBody String email) {
        signUpUserUseCase.resetPassword(email);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "Verification code resent."));
    }
}
