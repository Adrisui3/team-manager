package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.model.payments.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID> {
    List<PaymentJpaEntity> findAllByNextPaymentDateBeforeAndStatus(LocalDate date, PaymentStatus status);
}
