package com.manager.payments.application.service.players;

import com.manager.payments.application.port.in.players.FindPlayerUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FindPlayerService implements FindPlayerUseCase {

    private final PlayerRepository playerRepository;

    @Override
    public Player findById(UUID playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> PlayerNotFoundException.byId(playerId));
    }

    @Override
    public Page<Player> findAll(String query, Category category, PlayerGender gender, PlayerStatus status,
                                Boolean hasPendingReceipt, Boolean withoutPaymentAssigned, Boolean hasOverdueReceipt,
                                Pageable pageable) {
        return playerRepository.findAll(query.trim().toLowerCase(Locale.ROOT), category, gender, status,
                hasPendingReceipt, withoutPaymentAssigned, hasOverdueReceipt, pageable);
    }
}
