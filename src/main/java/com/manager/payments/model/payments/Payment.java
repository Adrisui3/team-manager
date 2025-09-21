package com.manager.payments.model.payments;

import com.manager.payments.model.players.PlayerMinInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Payment(UUID id, double amount, String name, String description, LocalDate startDate,
                      LocalDate nextPaymentDate, LocalDate endDate, int periodDays, PaymentStatus status,
                      List<PlayerMinInfo> players) {

    public boolean hasPlayer(UUID playerId) {
        return players.stream().anyMatch(user -> user.id().equals(playerId));
    }

    public Payment withNextPaymentDate(LocalDate nextPaymentDate) {
        return new Payment(id, amount, name, description, startDate, nextPaymentDate, endDate, periodDays, status,
                players);
    }

    public Payment withStatus(PaymentStatus status) {
        return new Payment(id, amount, name, description, startDate, nextPaymentDate, endDate, periodDays, status,
                players);
    }
}
