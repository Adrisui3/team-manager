package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.models.LoginResponseDto;
import com.manager.auth.adapter.dto.models.UserDto;
import com.manager.auth.adapter.dto.requests.ChangeUserPasswordRequestDto;
import com.manager.auth.adapter.dto.requests.LoginUserRequestDto;
import com.manager.auth.adapter.dto.requests.RegisterUserRequestDto;
import com.manager.auth.adapter.dto.requests.SetUserPasswordRequestDto;
import com.manager.auth.adapter.in.security.AuthenticatedUserProvider;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication management endpoints")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final SignUpUserUseCase signUpUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserMapper userMapper;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Operation(summary = "Create a new user", description = "Only users with ADMIN role can perform this action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "User already exists",
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
            @ApiResponse(responseCode = "400", description = "Invalid email or password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponseDto>> authenticate(@Valid @RequestBody LoginUserRequestDto loginUserRequestDto) {
        LoginResponseDto loginResponseDto = authenticateUserUseCase.authenticate(loginUserRequestDto);
        return ResponseEntity.ok(new ResponseDto<>(loginResponseDto));
    }

    @Operation(summary = "Set user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's password successfully set",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User or verification request not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Verification code expired or invalid",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PostMapping("/set-password")
    public ResponseEntity<ResponseDto<String>> setPassword(@Valid @RequestBody SetUserPasswordRequestDto setUserPasswordRequestDto) {
        signUpUserUseCase.setPassword(setUserPasswordRequestDto);
        return ResponseEntity.ok(new ResponseDto<>("Password set successfully,"));
    }

    @Operation(summary = "Change a user's password.", description = "A given user can only change their own password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully changed.", useReturnTypeSchema =
                    true),
            @ApiResponse(responseCode = "400", description = "Email or password do not match authenticated user's.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PutMapping("/change-password")
    public ResponseEntity<ResponseDto<String>> changePassword(@Valid @RequestBody ChangeUserPasswordRequestDto changeUserPasswordRequestDto) {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        signUpUserUseCase.changePassword(changeUserPasswordRequestDto, authenticatedUser);
        return ResponseEntity.ok(new ResponseDto<>("Password changed successfully,"));
    }

    @Operation(summary = "Reset a user's password", description = "Only users with ADMIN role can perform this action.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's password successfully reset",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<String>> resetPassword(@RequestBody String email) {
        signUpUserUseCase.resetPassword(email);
        return ResponseEntity.ok(new ResponseDto<>("Verification code resent."));
    }
}
