package com.manager.auth.adapter.in.rest.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "SetUserPasswordRequest", description = "Payload to set a user's password")
public record SetUserPasswordRequestDto(
        @NotBlank @Email @Schema(description = "Email") String email,
        @Schema(description = "Verification code") String verificationCode,
        @NotBlank @Size(min = 8, max = 60) @Schema(description = "Password") String password) {
}
