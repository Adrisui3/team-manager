package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID> {

    @Query("""
            select p
            from PaymentJpaEntity p
            where p.periodicity != 'ONCE' and p.status != 'EXPIRED' and p.endDate < :date
            """)
    List<PaymentJpaEntity> findAllExpired(@Param("date") LocalDate date);

    boolean existsByCode(String code);

    @Query("""
              select p
              from PaymentJpaEntity p
              where (lower(p.code) like concat(:query, '%')
                 or lower(p.name) like concat(concat('%', :query), '%')
                 or lower(p.description) like concat(concat('%', :query), '%'))
                 and (:status is null or p.status = :status)
                 and (:periodicity is null or p.periodicity = :periodicity)
            """)
    Page<PaymentJpaEntity> findAll(@Param("query") String query,
                                   @Param("status") PaymentStatus status,
                                   @Param("periodicity") Periodicity periodicity,
                                   Pageable pageable);
}
