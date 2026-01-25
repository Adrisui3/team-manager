package com.manager.payments.adapter.out.persistence.players;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PlayerJpaRepository extends JpaRepository<PlayerJpaEntity, UUID> {

    boolean existsByPersonalId(String personalId);

    boolean existsByEmail(String email);

    @Query("""
              select p
              from PlayerJpaEntity p
              where lower(p.personalId) like concat(:query, '%')
                 or lower(p.name) like concat(concat('%', :query), '%')
                 or lower(p.surname) like concat(concat('%', :query), '%')
                 or lower(p.email) like concat(concat('%', :query), '%')
            """)
    Page<PlayerJpaEntity> findAllByQuery(@Param("query") String query, Pageable pageable);
}
