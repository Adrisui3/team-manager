package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
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

    @Column(nullable = false)
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
}
