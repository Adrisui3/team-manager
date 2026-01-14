package com.manager.payments.adapter.out.persistence.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID> {

    @Query("""
            select p
            from PaymentJpaEntity p
            where p.periodicity != 'ONCE' and p.status != 'EXPIRED' and p.endDate < :date
            """)
    List<PaymentJpaEntity> findAllExpired(LocalDate date);

    boolean existsByCode(String code);
}
