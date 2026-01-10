package com.manager.auth.adapter.in.rest.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateUserRequest", description = "Payload to update a user")
public record UpdateUserRequestDto(
        @NotBlank @Size(min = 1, max = 60) @Schema(description = "Name") String name,
        @NotBlank @Size(min = 1, max = 80) @Schema(description = "Surname") String surname) {
}
