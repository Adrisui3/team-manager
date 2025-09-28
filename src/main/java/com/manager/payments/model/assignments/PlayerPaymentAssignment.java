package com.manager.payments.model.assignments;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;

import java.util.UUID;

public record PlayerPaymentAssignment(UUID id, Player player, Payment payment, boolean active) {

    public PlayerPaymentAssignment(Player player, Payment payment, boolean active) {
        this(null, player, payment, active);
    }
}
