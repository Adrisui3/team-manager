package com.manager.email.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EmailOutboxJpaRepository extends JpaRepository<EmailOutboxJpaEntity, UUID> {

    @Modifying
    @Query("""
            delete
            from EmailOutboxJpaEntity e
            where e.createdAt < :targetDate and e.status in (com.manager.email.model.EmailStatus.SENT, com.manager.email.model.EmailStatus.DISCARDED)
            """)
    int deleteExpired(@Param("targetDate") LocalDateTime targetDate);


    @Query("""
            select e
            from EmailOutboxJpaEntity e
            where :targetDate < e.createdAt and e.status in (com.manager.email.model.EmailStatus.PENDING, com.manager.email.model.EmailStatus.ERRORED)
            """)
    List<EmailOutboxJpaEntity> findAllToBeSent(@Param("targetDate") LocalDateTime targetDate);
}
