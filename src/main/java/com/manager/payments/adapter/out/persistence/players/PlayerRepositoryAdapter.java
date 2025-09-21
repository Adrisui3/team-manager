package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaEntity;
import com.manager.payments.adapter.out.persistence.payments.PaymentJpaRepository;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaRepository;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.players.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final ReceiptMapper receiptMapper;
    private final ReceiptJpaRepository receiptJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    private final PlayerJpaRepository playerJpaRepository;
    private final PlayerMapper playerMapper;

    public PlayerRepositoryAdapter(ReceiptMapper receiptMapper, ReceiptJpaRepository receiptJpaRepository,
                                   PaymentJpaRepository paymentJpaRepository,
                                   PlayerJpaRepository playerJpaRepository, PlayerMapper playerMapper) {
        this.receiptMapper = receiptMapper;
        this.receiptJpaRepository = receiptJpaRepository;
        this.paymentJpaRepository = paymentJpaRepository;
        this.playerJpaRepository = playerJpaRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public Player save(Player player) {
        PlayerJpaEntity playerJpaEntity = playerMapper.toPlayerJpaEntity(player, paymentJpaRepository,
                receiptJpaRepository);
        PlayerJpaEntity savedPlayerJpaEntity = playerJpaRepository.save(playerJpaEntity);
        return playerMapper.toPlayer(savedPlayerJpaEntity);
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return playerJpaRepository.findById(id).map(playerMapper::toPlayer);
    }

    @Override
    public Optional<Player> findByPersonalId(String personalId) {
        return playerJpaRepository.findByPersonalId(personalId).map(playerMapper::toPlayer);
    }

    @Override
    public void deleteById(UUID id) {
        Optional<PlayerJpaEntity> optionalPlayerJpaEntity = playerJpaRepository.findById(id);
        if (optionalPlayerJpaEntity.isPresent()) {
            PlayerJpaEntity playerJpaEntity = optionalPlayerJpaEntity.get();
            for (PaymentJpaEntity paymentJpaEntity : playerJpaEntity.getPayments()) {
                paymentJpaEntity.getPlayers().remove(playerJpaEntity);
                paymentJpaRepository.save(paymentJpaEntity);
            }

            playerJpaRepository.deleteById(id);
        }
    }

    @Override
    public List<ReceiptMinInfo> findAllReceipts(UUID userId) {
        PlayerJpaEntity playerJpaEntity =
                playerJpaRepository.findById(userId).orElseThrow(() -> new PlayerNotFoundException(userId));
        return playerJpaEntity.getReceipts().stream().map(receiptMapper::toReceiptMinInfo).toList();
    }
}
