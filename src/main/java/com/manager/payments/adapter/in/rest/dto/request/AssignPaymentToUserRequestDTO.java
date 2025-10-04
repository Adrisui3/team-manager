package com.manager.payments.adapter.in.rest.dto.request;

import java.util.UUID;

public record AssignPaymentToUserRequestDTO(UUID userId, UUID paymentId) {
}
