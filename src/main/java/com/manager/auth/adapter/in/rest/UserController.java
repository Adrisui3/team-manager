package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.models.UserDto;
import com.manager.auth.adapter.in.security.AuthenticatedUserProvider;
import com.manager.auth.adapter.out.persistence.mapper.UserMapper;
import com.manager.auth.model.users.User;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management endpoints")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserMapper userMapper;

    @Operation(summary = "Get authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user",
                    useReturnTypeSchema = true)
    })
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserDto>> getCurrentUser() {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        return ResponseEntity.ok(new ResponseDto<>(userMapper.toUserDto(authenticatedUser)));
    }
}
