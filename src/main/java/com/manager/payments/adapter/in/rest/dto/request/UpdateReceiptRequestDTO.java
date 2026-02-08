package com.manager.payments.adapter.in.rest.dto.request;

import com.manager.payments.model.receipts.ReceiptStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "UpdateReceiptRequest", description = "Payload to update a receipt")
public record UpdateReceiptRequestDTO(
        @NotNull @Schema(description = "Receipt amount") BigDecimal amount,
        @NotNull @Schema(description = "Expiry date") LocalDate expiryDate,
        @NotNull @Schema(description = "Receipt status", example = "PAID", implementation = ReceiptStatus.class) ReceiptStatus status
) {
}
