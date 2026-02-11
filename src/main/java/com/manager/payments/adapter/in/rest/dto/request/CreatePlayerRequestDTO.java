package com.manager.payments.adapter.in.rest.dto.request;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.PlayerGender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(name = "CreatePlayerRequest", description = "Payload to create a player")
public record CreatePlayerRequestDTO(
        @NotBlank @Schema(description = "Personal id", example = "12345678A") String personalId,
        @NotBlank @Size(min = 1, max = 60) @Schema(description = "Name") String name,
        @NotBlank @Size(min = 1, max = 80) @Schema(description = "Surname") String surname,
        @NotBlank @Email @Schema(description = "Email") String email,
        @Email @Schema(description = "Secondary email") String secondaryEmail,
        @NotNull @Schema(description = "Birth date") LocalDate birthDate,
        @NotBlank @Size(min = 9, max = 80) @Schema(description = "Phone number") String phoneNumber,
        @Size(min = 9, max = 80) @Schema(description = "Secondary phone number") String secondaryPhoneNumber,
        @NotNull @Schema(description = "Player category", example = "CADETE", implementation = Category.class) Category category,
        @NotNull @Schema(description = "Player gender", example = "MASCULINO", implementation = PlayerGender.class) PlayerGender gender) {
}
