package com.manager.payments.application.service.players;

import com.manager.payments.application.port.in.players.DeletePlayerUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePlayerService implements DeletePlayerUseCase {

    private final PlayerRepository playerRepository;

    @Override
    public void deleteById(UUID id) {
        if (!playerRepository.existsById(id)) {
            throw PlayerNotFoundException.byId(id);
        }

        playerRepository.deleteById(id);
    }
}
