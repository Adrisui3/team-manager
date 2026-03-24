package com.manager.auth.application.service.users;

import com.manager.auth.application.port.in.LinkPlayerToUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.PlayerAlreadyAssignedToUserException;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkPlayerToUserService implements LinkPlayerToUserUseCase {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    @Override
    public void linkPlayerToUser(UUID userId, UUID playerId) {
        User user = userRepository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> PlayerNotFoundException.byId(playerId));

        Optional<UUID> existingUserId = userRepository.findUserIdByPlayerId(player.id());
        if (existingUserId.isPresent()) {
            throw new PlayerAlreadyAssignedToUserException(playerId, userId);
        }

        userRepository.save(user.toBuilder().player(player).build());
    }

    @Override
    public void unlinkPlayerToUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> UserNotFound.byId(userId));
        userRepository.save(user.toBuilder().player(null).build());
    }
}
