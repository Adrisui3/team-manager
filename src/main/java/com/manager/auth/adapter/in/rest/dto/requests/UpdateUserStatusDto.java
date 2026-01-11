package com.manager.auth.adapter.in.rest.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "UpdateUserStatus", description = "Payload to update a user' status")
public record UpdateUserStatusDto(
        @NotNull @Schema(description = "User status") boolean enabled
) {
}
