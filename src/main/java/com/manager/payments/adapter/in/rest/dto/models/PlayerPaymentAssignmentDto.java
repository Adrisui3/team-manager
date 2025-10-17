package com.manager.payments.adapter.in.rest.dto.models;

import java.util.UUID;

public record PlayerPaymentAssignmentDto(UUID id, PlayerDto player, PaymentDto payment) {
}
