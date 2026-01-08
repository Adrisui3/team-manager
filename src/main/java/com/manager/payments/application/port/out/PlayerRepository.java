package com.manager.payments.application.port.out;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {

    Page<Player> findAllPlayers(Pageable pageable);

    Player save(Player player);

    Optional<Player> findById(UUID id);

    Optional<Player> findByEmail(String email);

    boolean existsById(UUID id);

    Optional<Player> findByPersonalId(String personalId);

    boolean existsByPersonalId(String personalId);

    boolean existsByEmail(String email);

    void deleteById(UUID id);

    List<Payment> findAllAssignedPayments(UUID playerId);
}
