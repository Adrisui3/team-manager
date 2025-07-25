package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.adapter.out.persistence.users.UserJpaEntity;
import com.manager.payments.model.payments.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class PaymentJpaEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private double amount;

    private String name;
    private String description;

    private LocalDateTime startDate;
    private LocalDateTime nextPaymentDate;
    private LocalDateTime endDate;

    private int periodDays;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.ACTIVE;

    @ManyToMany(mappedBy = "payments")
    private List<UserJpaEntity> users = new ArrayList<>();

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getPeriodDays() {
        return periodDays;
    }

    public void setPeriodDays(int periodDays) {
        this.periodDays = periodDays;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }

    public LocalDateTime getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDateTime nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public List<UserJpaEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserJpaEntity> userJpaEntities) {
        this.users = userJpaEntities;
    }
}
