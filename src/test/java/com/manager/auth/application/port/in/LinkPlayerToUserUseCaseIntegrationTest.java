package com.manager.auth.application.port.in;

import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.generator.UserGenerator;
import com.manager.auth.model.exceptions.PlayerAlreadyAssignedToUserException;
import com.manager.auth.model.exceptions.UserNotFound;
import com.manager.auth.model.users.User;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.generator.PlayerGenerator;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LinkPlayerToUserUseCaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LinkPlayerToUserUseCase linkPlayerToUserUseCase;

    @Test
    void shouldLinkPlayerToUser() {
        User user = userRepository.save(UserGenerator.user().id(null).build());
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());

        linkPlayerToUserUseCase.linkPlayerToUser(user.id(), player.id());

        assertThat(playerRepository.findById(player.id())).hasValueSatisfying(p ->
                assertThat(p.userId()).isEqualTo(user.id()));
    }

    @Test
    void shouldFailToLinkPlayerWhenUserNotFound() {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        UUID nonExistentUserId = UUID.randomUUID();

        assertThatThrownBy(() -> linkPlayerToUserUseCase.linkPlayerToUser(nonExistentUserId, player.id()))
                .isInstanceOf(UserNotFound.class);
    }

    @Test
    void shouldFailToLinkPlayerWhenPlayerNotFound() {
        User user = userRepository.save(UserGenerator.user().id(null).build());
        UUID nonExistentPlayerId = UUID.randomUUID();

        assertThatThrownBy(() -> linkPlayerToUserUseCase.linkPlayerToUser(user.id(), nonExistentPlayerId))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    void shouldFailToLinkPlayerWhenPlayerAlreadyAssigned() {
        User user = userRepository.save(UserGenerator.user().id(null).build());
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        playerRepository.save(player.toBuilder().userId(user.id()).build());

        assertThatThrownBy(() -> linkPlayerToUserUseCase.linkPlayerToUser(user.id(), player.id()))
                .isInstanceOf(PlayerAlreadyAssignedToUserException.class);
    }

    @Test
    void shouldUnlinkPlayerFromUser() {
        User user = userRepository.save(UserGenerator.user().id(null).build());
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        playerRepository.save(player.toBuilder().userId(user.id()).build());

        linkPlayerToUserUseCase.unlinkPlayerToUser(player.id());

        assertThat(playerRepository.findById(player.id())).hasValueSatisfying(p ->
                assertThat(p.userId()).isNull());
    }

    @Test
    void shouldFailToUnlinkPlayerWhenPlayerNotFound() {
        UUID nonExistentPlayerId = UUID.randomUUID();

        assertThatThrownBy(() -> linkPlayerToUserUseCase.unlinkPlayerToUser(nonExistentPlayerId))
                .isInstanceOf(PlayerNotFoundException.class);
    }
}
