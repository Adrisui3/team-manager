package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.adapter.out.persistence.assignments.PlayerPaymentAssignmentJpaEntity;
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

    private LocalDate periodStartDate;
    private LocalDate periodEndDate;

    @Enumerated(EnumType.STRING)
    private ReceiptStatus status = ReceiptStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_payment_id", nullable = false)
    private PlayerPaymentAssignmentJpaEntity playerPaymentAssignment;

    private boolean enabled;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public ReceiptStatus getStatus() {
        return status;
    }

    public void setStatus(ReceiptStatus status) {
        this.status = status;
    }

    public PlayerPaymentAssignmentJpaEntity getPlayerPaymentAssignment() {
        return playerPaymentAssignment;
    }

    public void setPlayerPaymentAssignment(PlayerPaymentAssignmentJpaEntity playerPaymentAssignment) {
        this.playerPaymentAssignment = playerPaymentAssignment;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
