package com.manager.payments.application.port.in.players;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FindPlayerUseCase {

    Player findById(UUID id);

    Page<Player> findAll(String query, Category category, PlayerGender gender, PlayerStatus status, Pageable pageable);
}
