package com.manager.payments.adapter.out.persistence.receipts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceiptJpaRepository extends JpaRepository<ReceiptJpaEntity, UUID> {
}
