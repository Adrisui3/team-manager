package com.manager.payments.application.service.players;

import com.manager.payments.adapter.in.rest.dto.request.UpdatePlayerRequestDTO;
import com.manager.payments.application.port.in.players.UpdatePlayerUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePlayerService implements UpdatePlayerUseCase {

    private final PlayerRepository playerRepository;

    @Override
    public Player updatePlayer(UUID playerId, UpdatePlayerRequestDTO request) {
        Player player =
                playerRepository.findById(playerId).orElseThrow(() -> PlayerNotFoundException.byId(playerId));

        Player updatedPlayer = player.toBuilder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .secondaryEmail(request.secondaryEmail())
                .birthDate(request.birthDate())
                .phoneNumber(request.phoneNumber())
                .secondaryPhoneNumber(request.secondaryPhoneNumber())
                .category(request.category())
                .status(request.status())
                .gender(request.gender())
                .build();

        return playerRepository.save(updatedPlayer);
    }
}
