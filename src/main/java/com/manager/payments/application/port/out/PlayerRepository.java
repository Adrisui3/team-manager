package com.manager.payments.application.port.out;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {

    Player save(Player player);

    Optional<Player> findById(UUID id);

    boolean existsById(UUID id);

    boolean existsByPersonalId(String personalId);

    boolean existsByEmail(String email);

    void deleteById(UUID id);

    Page<Player> findAll(String query, Category category, PlayerGender gender, PlayerStatus status, Pageable pageable);
}
