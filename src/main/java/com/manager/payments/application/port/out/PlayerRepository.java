package com.manager.payments.application.port.out;

import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {

    Player save(Player player);

    Optional<Player> findById(UUID id);

    Optional<Player> findByPersonalId(String personalId);

    void deleteById(UUID id);

    List<Receipt> findAllReceipts(UUID userId);
}
