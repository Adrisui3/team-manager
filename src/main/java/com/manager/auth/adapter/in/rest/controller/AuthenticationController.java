package com.manager.auth.adapter.in.rest.controller;

import com.manager.auth.adapter.in.rest.dto.models.LoginResponseDto;
import com.manager.auth.adapter.in.rest.dto.models.UserDto;
import com.manager.auth.adapter.in.rest.dto.requests.LoginUserRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.RegisterUserRequestDto;
import com.manager.auth.adapter.out.persistence.mapper.UserMapper;
import com.manager.auth.application.port.in.AuthenticateUserUseCase;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.model.users.User;
import com.manager.shared.response.ErrorResponse;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management endpoints")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final SignUpUserUseCase signUpUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserMapper userMapper;

    @Operation(summary = "Create a new user", description = "Only users with ADMIN role can perform this action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<UserDto>> registerUser(@Valid @RequestBody RegisterUserRequestDto registerUserRequestDto) {
        User registeredUser = signUpUserUseCase.signup(registerUserRequestDto);
        return ResponseEntity.ok(new ResponseDto<>(userMapper.toUserDto(registeredUser)));
    }

    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Invalid email or password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "User disabled.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponseDto>> authenticate(@Valid @RequestBody LoginUserRequestDto loginUserRequestDto) {
        LoginResponseDto loginResponseDto = authenticateUserUseCase.authenticate(loginUserRequestDto);
        return ResponseEntity.ok(new ResponseDto<>(loginResponseDto));
    }
}
