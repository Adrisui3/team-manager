package com.manager.payments.adapter.out.persistence.players;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PlayerJpaRepository extends JpaRepository<PlayerJpaEntity, UUID> {

    boolean existsByPersonalId(String personalId);

    boolean existsByEmail(String email);

    @Query("""
              select p
              from PlayerJpaEntity p
              where lower(p.personalId) like concat(:q, '%')
                 or lower(p.name) like concat(concat('%', :q), '%')
                 or lower(p.surname) like concat(concat('%', :q), '%')
                 or lower(p.email) like concat(concat('%', :q), '%')
            """)
    Page<PlayerJpaEntity> findAllByQuery(String q, Pageable pageable);
}
