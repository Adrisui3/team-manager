package com.manager.auth.adapter.out.persistence.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
                select u
                from UserJpaEntity u
                where lower(u.email) like concat(concat('%', :query), '%')
                   or lower(u.name) like concat(concat('%', :query), '%')
                   or lower(u.surname) like concat(concat('%', :query), '%')
            """)
    Page<UserJpaEntity> findAll(@Param("query") String query, Pageable pageable);
}
