package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.adapter.out.persistence.assignments.PlayerPaymentAssignmentJpaEntity;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payment")
public class PaymentJpaEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false, length = 10)
    private String code;

    @Column(nullable = false)
    private BigDecimal amount;

    private String name;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Periodicity periodicity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerPaymentAssignmentJpaEntity> assignments = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public List<PlayerPaymentAssignmentJpaEntity> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<PlayerPaymentAssignmentJpaEntity> assignments) {
        this.assignments = assignments;
    }
}
