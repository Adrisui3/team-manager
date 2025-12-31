package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.models.UserDto;
import com.manager.auth.adapter.in.security.AuthenticatedUserProvider;
import com.manager.auth.adapter.out.persistence.mapper.UserMapper;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.users.User;
import com.manager.shared.response.PageResponse;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management endpoints")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserMapper mapper;
    private final UserRepository repository;

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
}
