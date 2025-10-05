package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaRepository;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerPaymentAssignmentRepositoryAdapter implements PlayerPaymentAssignmentRepository {

    private final ReceiptMapper receiptMapper;
    private final PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper;
    private final PlayerPaymentAssignmentJpaRepository playerPaymentAssignmentJpaRepository;
    private final ReceiptJpaRepository receiptJpaRepository;

    public PlayerPaymentAssignmentRepositoryAdapter(ReceiptMapper receiptMapper,
                                                    PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper,
                                                    PlayerPaymentAssignmentJpaRepository playerPaymentAssignmentJpaRepository, ReceiptJpaRepository receiptJpaRepository) {
        this.receiptMapper = receiptMapper;
        this.playerPaymentAssignmentMapper = playerPaymentAssignmentMapper;
        this.playerPaymentAssignmentJpaRepository = playerPaymentAssignmentJpaRepository;
        this.receiptJpaRepository = receiptJpaRepository;
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
    public boolean existsByPlayerAndPayment(Player player, Payment payment) {
        return playerPaymentAssignmentJpaRepository.existsByPlayer_IdAndPayment_Id(player.id(), payment.id());
    }

    @Override
    public List<PlayerPaymentAssignment> findAllActiveAndStartDateBeforeOrEqual(LocalDate date) {
        List<PlayerPaymentAssignmentJpaEntity> playerPaymentAssignmentJpaEntities =
                playerPaymentAssignmentJpaRepository.findAllByActiveIsTrueAndPayment_StartDateLessThanEqual(date);
        return playerPaymentAssignmentJpaEntities.stream().map(playerPaymentAssignmentMapper::toPlayerPaymentAssignment).toList();
    }
}
