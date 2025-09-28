package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final PlayerJpaRepository playerJpaRepository;
    private final PlayerMapper playerMapper;

    public PlayerRepositoryAdapter(PlayerJpaRepository playerJpaRepository, PlayerMapper playerMapper) {
        this.playerJpaRepository = playerJpaRepository;
        this.playerMapper = playerMapper;
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

    @Override
    public List<Receipt> findAllReceipts(UUID userId) {
        return Collections.emptyList();
    }
}
