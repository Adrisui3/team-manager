package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaEntity;
import com.manager.payments.adapter.out.persistence.players.PlayerJpaEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "player_payment_assignment",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_player_payment",
                columnNames = {"player_id", "payment_id"}
        ))
public class PlayerPaymentAssignmentJpaEntity {

    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerJpaEntity player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentJpaEntity payment;

    private boolean active;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PlayerJpaEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerJpaEntity player) {
        this.player = player;
    }

    public PaymentJpaEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentJpaEntity payment) {
        this.payment = payment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
