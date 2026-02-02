package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
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
              where (lower(p.personalId) like concat(:query, '%')
                 or lower(p.name) like concat(concat('%', :query), '%')
                 or lower(p.surname) like concat(concat('%', :query), '%')
                 or lower(p.email) like concat(concat('%', :query), '%'))
                 and (:status is null or p.status = :status)
                 and (:gender is null or p.gender = :gender)
                 and (:category is null or p.category = :category)
                 and (:hasPendingReceipt is null or :hasPendingReceipt = false or exists (
                     select 1 from ReceiptJpaEntity r where r.player = p and r.status = 'PENDING'))
                 and (:withoutPaymentAssigned is null or :withoutPaymentAssigned = false or not exists (
                     select 1 from PlayerPaymentAssignmentJpaEntity a where a.player = p))
                 and (:hasOverdueReceipt is null or :hasOverdueReceipt = false or exists (
                     select 1 from ReceiptJpaEntity r where r.player = p and r.status = 'OVERDUE'))
            """)
    Page<PlayerJpaEntity> findAll(@Param("query") String query, @Param("status") PlayerStatus status,
                                  @Param("gender") PlayerGender gender, @Param("category") Category category,
                                  @Param("hasPendingReceipt") Boolean hasPendingReceipt,
                                  @Param("withoutPaymentAssigned") Boolean withoutPaymentAssigned,
                                  @Param("hasOverdueReceipt") Boolean hasOverdueReceipt,
                                  Pageable pageable);
}
