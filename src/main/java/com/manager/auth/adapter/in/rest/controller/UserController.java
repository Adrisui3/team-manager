package com.manager.auth.adapter.in.rest.controller;

import com.manager.auth.adapter.in.rest.dto.models.UserDto;
import com.manager.auth.adapter.in.rest.dto.requests.ChangeUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.SetUserPasswordRequestDto;
import com.manager.auth.adapter.in.rest.dto.requests.UpdateUserRequestDto;
import com.manager.auth.adapter.in.security.AuthenticatedUserProvider;
import com.manager.auth.adapter.out.persistence.mapper.UserMapper;
import com.manager.auth.application.port.in.UpdateUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import com.manager.shared.response.ErrorResponse;
import com.manager.shared.response.PageResponse;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Users management endpoints")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserMapper mapper;
    private final UserRepository repository;
    private final UpdateUserUseCase updateUserUseCase;

    @Operation(summary = "Get authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user",
                    useReturnTypeSchema = true)
    })
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserDto>> getCurrentUser() {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        return ResponseEntity.ok(new ResponseDto<>(mapper.toUserDto(authenticatedUser)));
    }

    @Operation(summary = "Get all users", description = "Supports pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of users", useReturnTypeSchema = true)
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserDto>> findAll(@ParameterObject Pageable pageable) {
        Page<User> users = repository.findAll(pageable);
        return ResponseEntity.ok(PageResponse.of(users.map(mapper::toUserDto)));
    }

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<UserDto>> getUserById(@PathVariable UUID userId) {
        User user = repository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));
        return ResponseEntity.ok(new ResponseDto<>(mapper.toUserDto(user)));
    }

    @Operation(summary = "Update user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation =
                    ErrorResponse.class)))
    })
    @PutMapping("/{userId}")
    @PreAuthorize("@authz.canUpdateUser(#userId)")
    public ResponseEntity<ResponseDto<UserDto>> updateUser(@PathVariable UUID userId,
                                                           @Valid @RequestBody UpdateUserRequestDto request) {
        User user = updateUserUseCase.updateUser(userId, request);
        return ResponseEntity.ok(new ResponseDto<>(mapper.toUserDto(user)));
    }

    @Operation(summary = "Set user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's password successfully set",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User or verification request not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
            @ApiResponse(responseCode = "410", description = "Verification code expired",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Verification code invalid",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PostMapping("/set-password")
    public ResponseEntity<ResponseDto<String>> setPassword(@Valid @RequestBody SetUserPasswordRequestDto setUserPasswordRequestDto) {
        updateUserUseCase.setPassword(setUserPasswordRequestDto);
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
    @PutMapping("/change-password/{userId}")
    @PreAuthorize("@authz.isCurrentUser(#userId)")
    public ResponseEntity<ResponseDto<String>> changePassword(@PathVariable UUID userId,
                                                              @Valid @RequestBody ChangeUserPasswordRequestDto changeUserPasswordRequestDto) {
        updateUserUseCase.changePassword(userId, changeUserPasswordRequestDto);
        return ResponseEntity.ok(new ResponseDto<>("Password changed successfully,"));
    }

    @Operation(summary = "Reset a user's password", description = "Only users with ADMIN role can perform this action.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's password successfully reset",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "User disabled.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<String>> resetPassword(@RequestBody String email) {
        updateUserUseCase.resetPassword(email);
        return ResponseEntity.ok(new ResponseDto<>("Verification code resent."));
    }
}
