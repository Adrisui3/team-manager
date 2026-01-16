package com.manager.payments.application.port.in;

import com.manager.payments.model.players.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FindPlayerUseCase {

    Player findById(UUID id);

    Page<Player> findAll(String query, Pageable pageable);
}
