package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaEntity;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.adapter.out.persistence.players.PlayerJpaEntity;
import com.manager.payments.adapter.out.persistence.players.PlayerMapper;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaEntity;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerPaymentAssignmentRepositoryAdapter implements PlayerPaymentAssignmentRepository {

    private final ReceiptMapper receiptMapper;
    private final PaymentMapper paymentMapper;
    private final PlayerMapper playerMapper;
    private final PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper;
    private final PlayerPaymentAssignmentJpaRepository playerPaymentAssignmentJpaRepository;

    public PlayerPaymentAssignmentRepositoryAdapter(ReceiptMapper receiptMapper, PaymentMapper paymentMapper,
                                                    PlayerMapper playerMapper,
                                                    PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper,
                                                    PlayerPaymentAssignmentJpaRepository playerPaymentAssignmentJpaRepository) {
        this.receiptMapper = receiptMapper;
        this.paymentMapper = paymentMapper;
        this.playerMapper = playerMapper;
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
    public List<PlayerPaymentAssignment> findByPlayer(Player player) {
        PlayerJpaEntity playerJpaEntity = playerMapper.toPlayerJpaEntity(player);
        return playerPaymentAssignmentJpaRepository.findAllByPlayer(playerJpaEntity).stream().map(playerPaymentAssignmentMapper::toPlayerPaymentAssignment).toList();
    }

    @Override
    public List<PlayerPaymentAssignment> findByPayment(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = paymentMapper.toPaymentJpaEntity(payment);
        return playerPaymentAssignmentJpaRepository.findAllByPayment(paymentJpaEntity).stream().map(playerPaymentAssignmentMapper::toPlayerPaymentAssignment).toList();
    }

    @Override
    public Optional<PlayerPaymentAssignment> findByPlayerAndPayment(Player player, Payment payment) {
        PlayerJpaEntity playerJpaEntity = playerMapper.toPlayerJpaEntity(player);
        PaymentJpaEntity paymentJpaEntity = paymentMapper.toPaymentJpaEntity(payment);
        return playerPaymentAssignmentJpaRepository.findByPlayerAndPayment(playerJpaEntity, paymentJpaEntity).map(playerPaymentAssignmentMapper::toPlayerPaymentAssignment);
    }

    @Override
    public void addReceipt(UUID playerPaymentAssignmentId, Receipt receipt) {
        PlayerPaymentAssignmentJpaEntity playerPaymentAssignmentJpaEntity =
                playerPaymentAssignmentJpaRepository.findById(playerPaymentAssignmentId).orElseThrow(() -> new RuntimeException("Player Payment Assignment not found"));
        ReceiptJpaEntity receiptJpaEntity = receiptMapper.toReceiptJpaEntity(receipt);
        playerPaymentAssignmentJpaEntity.getReceipts().add(receiptJpaEntity);

        playerPaymentAssignmentJpaRepository.save(playerPaymentAssignmentJpaEntity);
    }

    @Override
    public List<Receipt> findAllReceiptsByPlayer(Player player) {
        PlayerJpaEntity playerJpaEntity = playerMapper.toPlayerJpaEntity(player);
        List<PlayerPaymentAssignmentJpaEntity> playerPaymentAssignmentJpaEntities =
                playerPaymentAssignmentJpaRepository.findAllByPlayer(playerJpaEntity);
        return playerPaymentAssignmentJpaEntities.stream().flatMap(playerPaymentAssignmentJpaEntity -> playerPaymentAssignmentJpaEntity.getReceipts().stream()).map(receiptMapper::toReceipt).toList();
    }

    @Override
    public List<Receipt> findAllReceiptsByPayment(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = paymentMapper.toPaymentJpaEntity(payment);
        List<PlayerPaymentAssignmentJpaEntity> playerPaymentAssignmentJpaEntities =
                playerPaymentAssignmentJpaRepository.findAllByPayment(paymentJpaEntity);

        return playerPaymentAssignmentJpaEntities.stream().flatMap(playerPaymentAssignmentJpaEntity -> playerPaymentAssignmentJpaEntity.getReceipts().stream()).map(receiptMapper::toReceipt).toList();
    }
}
