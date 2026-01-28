package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class PlayerPaymentAssignmentRepositoryAdapter implements PlayerPaymentAssignmentRepository {

    private final PlayerPaymentAssignmentMapper mapper;
    private final PlayerPaymentAssignmentJpaRepository repository;

    public PlayerPaymentAssignmentRepositoryAdapter(PlayerPaymentAssignmentMapper mapper,
                                                    PlayerPaymentAssignmentJpaRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public PlayerPaymentAssignment save(PlayerPaymentAssignment playerPaymentAssignment) {
        PlayerPaymentAssignmentJpaEntity playerPaymentAssignmentJpaEntity =
                mapper.toPlayerPaymentAssignmentJpaEntity(playerPaymentAssignment);

        PlayerPaymentAssignmentJpaEntity savedEntity =
                repository.save(playerPaymentAssignmentJpaEntity);
        return mapper.toPlayerPaymentAssignment(savedEntity);
    }

    @Override
    public boolean existsByPlayerIdAndPaymentId(UUID playerId, UUID paymentId) {
        return repository.existsByPlayer_IdAndPayment_Id(playerId, paymentId);
    }

    @Override
    public List<PlayerPaymentAssignment> findAllForBilling(LocalDate date) {
        List<PlayerPaymentAssignmentJpaEntity> playerPaymentAssignmentJpaEntities = repository.findAllForBilling(date);
        return playerPaymentAssignmentJpaEntities.stream().map(mapper::toPlayerPaymentAssignment).toList();
    }

    @Override
    public void deleteByPlayerIdAndPaymentId(UUID playerId, UUID paymentId) {
        repository.deleteByPlayer_IdAndPayment_Id(playerId, paymentId);
    }

    @Override
    public Page<Payment> findAllPaymentsByPlayerId(UUID playerId, Pageable pageable) {
        Page<PlayerPaymentAssignmentJpaEntity> assignments = repository.findAllByPlayer_Id(playerId, pageable);
        return assignments.map(assignment -> mapper.toPlayerPaymentAssignment(assignment).payment());
    }
}
