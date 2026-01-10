package com.manager.auth.adapter.in.rest.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "ChangeUserPasswordRequest", description = "Payload to change a user's password")
public record ChangeUserPasswordRequestDto(
        @NotBlank @Size(min = 8, max = 60) @Schema(description = "Password") String oldPassword,
        @NotBlank @Size(min = 8, max = 60) @Schema(description = "Password") String newPassword) {
}
