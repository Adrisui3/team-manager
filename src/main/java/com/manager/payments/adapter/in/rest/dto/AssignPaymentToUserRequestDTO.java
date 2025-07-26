package com.manager.payments.adapter.in.rest.dto;

import java.util.UUID;

public record AssignPaymentToUserRequestDTO(UUID userId, UUID paymentId) {
}
