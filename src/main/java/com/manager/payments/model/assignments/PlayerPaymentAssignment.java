package com.manager.payments.model.assignments;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record PlayerPaymentAssignment(UUID id, Player player, Payment payment) {
}
