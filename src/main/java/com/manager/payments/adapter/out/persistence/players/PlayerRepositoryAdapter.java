package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.adapter.out.persistence.assignments.PlayerPaymentAssignmentJpaEntity;
import com.manager.payments.adapter.out.persistence.payments.PaymentJpaEntity;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final PlayerJpaRepository playerJpaRepository;
    private final PlayerMapper playerMapper;
    private final PaymentMapper paymentMapper;

    public PlayerRepositoryAdapter(PlayerJpaRepository playerJpaRepository, PlayerMapper playerMapper,
                                   PaymentMapper paymentMapper) {
        this.playerJpaRepository = playerJpaRepository;
        this.playerMapper = playerMapper;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Page<Player> findAllPlayers(Pageable pageable) {
        Page<PlayerJpaEntity> players = playerJpaRepository.findAll(pageable);
        return players.map(playerMapper::toPlayer);
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
    public boolean existsById(UUID id) {
        return playerJpaRepository.existsById(id);
    }

    @Override
    public Optional<Player> findByPersonalId(String personalId) {
        return playerJpaRepository.findByPersonalId(personalId).map(playerMapper::toPlayer);
    }

    @Override
    public void deleteById(UUID id) {
        if (!playerJpaRepository.existsById(id)) {
            throw new PlayerNotFoundException(id);
        }

        playerJpaRepository.deleteById(id);
    }

    @Override
    public List<Payment> findAllAssignedPayments(UUID playerId) {
        List<PaymentJpaEntity> assignedPayments =
                playerJpaRepository.findById(playerId).map(player -> player.getAssignments().stream().map(PlayerPaymentAssignmentJpaEntity::getPayment).toList()).orElseThrow(() -> new PlayerNotFoundException(playerId));
        return assignedPayments.stream().map(paymentMapper::toPayment).toList();
    }
}
