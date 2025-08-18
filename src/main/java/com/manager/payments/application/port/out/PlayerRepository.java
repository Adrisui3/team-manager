package com.manager.payments.application.port.out;

import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {

    Player save(Player player);

    Optional<Player> findById(UUID id);

    Optional<Player> findByPersonalId(String personalId);

    void deleteById(UUID id);

    List<ReceiptMinInfo> findAllReceipts(UUID userId);
}
