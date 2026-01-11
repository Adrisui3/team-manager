package com.manager.payments.adapter.in.rest.dto.request;

import com.manager.payments.model.payments.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdatePaymentRequest", description = "Payload to update a payment")
public record UpdatePaymentRequestDTO(
        @NotBlank @Size(min = 1, max = 60) @Schema(description = "Name") String name,
        @Size(max = 255) @Schema(description = "Description") String description,
        @NotNull @Schema(description = "Payment status", example = "ACTIVE", implementation = PaymentStatus.class) PaymentStatus status
) {
}
