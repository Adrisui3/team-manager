package com.manager.payments.adapter.in.rest.dto.request;

import com.manager.payments.model.payments.Periodicity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(name = "CreatePaymentRequest", description = "Payload to create a payment")
public record CreatePaymentRequestDTO(
        @NotNull double amount,
        @NotBlank @Size(min = 1, max = 10) @Schema(description = "Unique identifier") String code,
        @NotBlank @Size(min = 1, max = 60) @Schema(description = "Name") String name,
        @Size(max = 255) @Schema(description = "Description") String description,
        @NotNull @Schema(description = "Payment start date") LocalDate startDate,
        @NotNull @Schema(description = "Payment end date") LocalDate endDate,
        @NotNull @Schema(description = "Payment periodicity", example = "MONTHLY",
                implementation = Periodicity.class) Periodicity periodicity) {
}
