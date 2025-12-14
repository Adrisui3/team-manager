package com.manager.auth.adapter.dto.requests;

import com.manager.auth.model.roles.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "RegisterUserRequest", description = "Payload to register a user")
public record RegisterUserRequestDto(
        @NotBlank @Email @Schema(description = "Email") String email,
        @NotBlank @Size(min = 1, max = 60) @Schema(description = "Name") String name,
        @NotBlank @Size(min = 1, max = 80) @Schema(description = "Surname") String surname,
        @NotNull @Schema(description = "User role", example = "USER", implementation = Role.class) Role role) {

}
