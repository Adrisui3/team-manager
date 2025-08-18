package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.adapter.out.persistence.players.PlayerJpaEntity;
import com.manager.payments.model.receipts.ReceiptStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "receipt")
public class ReceiptJpaEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private double amount;
    private LocalDate issuedDate;
    private LocalDate paymentDate;
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private ReceiptStatus status = ReceiptStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerJpaEntity player;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public ReceiptStatus getStatus() {
        return status;
    }

    public void setStatus(ReceiptStatus status) {
        this.status = status;
    }

    public PlayerJpaEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerJpaEntity playerJpaEntity) {
        this.player = playerJpaEntity;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
