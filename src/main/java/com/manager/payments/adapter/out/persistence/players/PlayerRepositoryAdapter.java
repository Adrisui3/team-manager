package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.players.Player;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final PlayerJpaRepository playerJpaRepository;
    private final PlayerMapper playerMapper;
    private final ReceiptMapper receiptMapper;

    public PlayerRepositoryAdapter(PlayerJpaRepository playerJpaRepository, PlayerMapper playerMapper,
                                   ReceiptMapper receiptMapper) {
        this.playerJpaRepository = playerJpaRepository;
        this.playerMapper = playerMapper;
        this.receiptMapper = receiptMapper;
    }

    @Override
    public Player save(Player player) {
        PlayerJpaEntity playerJpaEntity = playerMapper.toPlayerJpaEntity(player);
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
        playerJpaRepository.deleteById(id);
    }
}
