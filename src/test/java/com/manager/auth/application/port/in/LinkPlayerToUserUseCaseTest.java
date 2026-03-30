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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LinkPlayerToUserUseCaseTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

    @Autowired
    private LinkPlayerToUserUseCase linkPlayerToUserUseCase;

    @Captor
    private ArgumentCaptor<Player> captor;

    @Test
    void shouldLinkPlayerToUser() {
        User user = UserGenerator.user().build();
        Player player = PlayerGenerator.player().build();

        when(userRepository.existsById(user.id())).thenReturn(true);
        when(playerRepository.findById(player.id())).thenReturn(Optional.of(player));

        linkPlayerToUserUseCase.linkPlayerToUser(user.id(), player.id());

        verify(playerRepository).save(captor.capture());
        assertThat(captor.getValue().userId()).isEqualTo(user.id());
    }

    @Test
    void shouldFailToLinkPlayerWhenUserNotFound() {
        UUID nonExistentUserId = UUID.randomUUID();
        Player player = PlayerGenerator.player().build();

        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);

        assertThatThrownBy(() -> linkPlayerToUserUseCase.linkPlayerToUser(nonExistentUserId, player.id()))
                .isInstanceOf(UserNotFound.class);

        verify(playerRepository, never()).save(any());
    }

    @Test
    void shouldFailToLinkPlayerWhenPlayerNotFound() {
        User user = UserGenerator.user().build();
        UUID nonExistentPlayerId = UUID.randomUUID();

        when(userRepository.existsById(user.id())).thenReturn(true);
        when(playerRepository.findById(nonExistentPlayerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkPlayerToUserUseCase.linkPlayerToUser(user.id(), nonExistentPlayerId))
                .isInstanceOf(PlayerNotFoundException.class);

        verify(playerRepository, never()).save(any());
    }

    @Test
    void shouldFailToLinkPlayerWhenPlayerAlreadyAssigned() {
        User user = UserGenerator.user().build();
        UUID existingUserId = UUID.randomUUID();
        Player player = PlayerGenerator.player().build().toBuilder().userId(existingUserId).build();

        when(userRepository.existsById(user.id())).thenReturn(true);
        when(playerRepository.findById(player.id())).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> linkPlayerToUserUseCase.linkPlayerToUser(user.id(), player.id()))
                .isInstanceOf(PlayerAlreadyAssignedToUserException.class);

        verify(playerRepository, never()).save(any());
    }

    @Test
    void shouldUnlinkPlayerFromUser() {
        UUID existingUserId = UUID.randomUUID();
        Player player = PlayerGenerator.player().build().toBuilder().userId(existingUserId).build();

        when(playerRepository.findById(player.id())).thenReturn(Optional.of(player));

        linkPlayerToUserUseCase.unlinkPlayerToUser(player.id());

        verify(playerRepository).save(captor.capture());
        assertThat(captor.getValue().userId()).isNull();
    }

    @Test
    void shouldFailToUnlinkPlayerWhenPlayerNotFound() {
        UUID nonExistentPlayerId = UUID.randomUUID();

        when(playerRepository.findById(nonExistentPlayerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkPlayerToUserUseCase.unlinkPlayerToUser(nonExistentPlayerId))
                .isInstanceOf(PlayerNotFoundException.class);

        verify(playerRepository, never()).save(any());
    }
}
