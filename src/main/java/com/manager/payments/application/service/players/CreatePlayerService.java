package com.manager.payments.application.service.players;

import com.manager.payments.adapter.in.rest.dto.request.CreatePlayerRequestDTO;
import com.manager.payments.application.port.in.players.CreatePlayerUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerAlreadyExistsException;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePlayerService implements CreatePlayerUseCase {

    private final PlayerRepository playerRepository;

    @Override
    public Player createPlayer(CreatePlayerRequestDTO request) {
        if (playerRepository.existsByPersonalId(request.personalId()))
            throw PlayerAlreadyExistsException.byPersonalId(request.personalId());

        Player newPlayer = Player.builder()
                .personalId(request.personalId())
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .secondaryEmail(request.secondaryEmail())
                .birthDate(request.birthDate())
                .phoneNumber(request.phoneNumber())
                .secondaryPhoneNumber(request.secondaryPhoneNumber())
                .category(request.category())
                .gender(request.gender())
                .status(PlayerStatus.ENABLED)
                .build();

        return playerRepository.save(newPlayer);
    }
}
