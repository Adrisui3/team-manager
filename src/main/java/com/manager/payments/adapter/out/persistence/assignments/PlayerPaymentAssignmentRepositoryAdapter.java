package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.players.PlayerStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlayerPaymentAssignmentRepositoryAdapter implements PlayerPaymentAssignmentRepository {

    private final PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper;
    private final PlayerPaymentAssignmentJpaRepository playerPaymentAssignmentJpaRepository;

    public PlayerPaymentAssignmentRepositoryAdapter(PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper,
                                                    PlayerPaymentAssignmentJpaRepository playerPaymentAssignmentJpaRepository) {
        this.playerPaymentAssignmentMapper = playerPaymentAssignmentMapper;
        this.playerPaymentAssignmentJpaRepository = playerPaymentAssignmentJpaRepository;
    }

    @Override
    public PlayerPaymentAssignment save(PlayerPaymentAssignment playerPaymentAssignment) {
        PlayerPaymentAssignmentJpaEntity playerPaymentAssignmentJpaEntity =
                playerPaymentAssignmentMapper.toPlayerPaymentAssignmentJpaEntity(playerPaymentAssignment);

        PlayerPaymentAssignmentJpaEntity savedEntity =
                playerPaymentAssignmentJpaRepository.save(playerPaymentAssignmentJpaEntity);
        return playerPaymentAssignmentMapper.toPlayerPaymentAssignment(savedEntity);
    }

    @Override
    public Optional<PlayerPaymentAssignment> findById(UUID id) {
        return playerPaymentAssignmentJpaRepository.findById(id).map(playerPaymentAssignmentMapper::toPlayerPaymentAssignment);
    }

    @Override
    public boolean existsByPlayerIdAndPaymentId(UUID playerId, UUID paymentId) {
        return playerPaymentAssignmentJpaRepository.existsByPlayer_IdAndPayment_Id(playerId, paymentId);
    }

    @Override
    public List<PlayerPaymentAssignment> findAllActiveAndStartDateBeforeOrEqual(LocalDate date) {
        List<PlayerPaymentAssignmentJpaEntity> playerPaymentAssignmentJpaEntities =
                playerPaymentAssignmentJpaRepository.findAllByPlayer_StatusAndPayment_StatusAndPayment_StartDateLessThanEqual(PlayerStatus.ENABLED, PaymentStatus.ACTIVE, date);
        return playerPaymentAssignmentJpaEntities.stream().map(playerPaymentAssignmentMapper::toPlayerPaymentAssignment).toList();
    }

    @Override
    public void deleteByPlayerIdAndPaymentId(UUID playerId, UUID paymentId) {
        playerPaymentAssignmentJpaRepository.deleteByPlayer_IdAndPayment_Id(playerId, paymentId);
    }
}
