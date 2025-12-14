package com.manager.auth.adapter.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "LoginUserRequest", description = "Payload to login")
public record LoginUserRequestDto(
        @NotBlank @Email @Schema(description = "Email") String email,
        @NotBlank @Size(min = 8, max = 60) @Schema(description = "Password") String password) {
}
