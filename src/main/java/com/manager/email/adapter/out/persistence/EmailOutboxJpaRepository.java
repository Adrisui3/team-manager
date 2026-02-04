package com.manager.email.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EmailOutboxJpaRepository extends JpaRepository<EmailOutboxJpaEntity, UUID> {

    @Query("""
            select e
            from EmailOutboxJpaEntity e
            where :targetDate < e.createdAt and e.status in ('PENDING', 'ERRORED')
            """)
    List<EmailOutboxJpaEntity> findAllToBeSent(@Param("targetDate") LocalDateTime targetDate);
}
