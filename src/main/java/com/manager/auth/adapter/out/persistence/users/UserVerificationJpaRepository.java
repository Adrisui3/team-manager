package com.manager.auth.adapter.out.persistence.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserVerificationJpaRepository extends JpaRepository<UserVerificationJpaEntity, UUID> {

    boolean existsByUser(UserJpaEntity user);

    void deleteByUser(UserJpaEntity user);

}
