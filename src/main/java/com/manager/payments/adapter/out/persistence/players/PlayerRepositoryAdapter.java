package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final PlayerJpaRepository repository;
    private final PlayerMapper mapper;

    @Override
    public Player save(Player player) {
        PlayerJpaEntity playerJpaEntity = mapper.toPlayerJpaEntity(player);
        PlayerJpaEntity savedPlayerJpaEntity = repository.save(playerJpaEntity);
        return mapper.toPlayer(savedPlayerJpaEntity);
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return repository.findById(id).map(mapper::toPlayer);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByPersonalId(String personalId) {
        return repository.existsByPersonalId(personalId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw PlayerNotFoundException.byId(id);
        }

        repository.deleteById(id);
    }

    @Override
    public Page<Player> findAll(String query, Category category, PlayerGender gender, PlayerStatus status,
                                Boolean hasPendingReceipt, Boolean withoutPaymentAssigned, Boolean hasOverdueReceipt,
                                Pageable pageable) {
        Page<PlayerJpaEntity> players = repository.findAll(query, status, gender, category, hasPendingReceipt,
                withoutPaymentAssigned, hasOverdueReceipt, pageable);
        return players.map(mapper::toPlayer);
    }
}
