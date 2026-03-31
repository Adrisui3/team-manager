package com.manager.auth.application.port.in;

import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.generator.UserGenerator;
import com.manager.auth.model.exceptions.PlayerAlreadyAssignedToUserException;
import com.manager.auth.model.exceptions.UserAlreadyExists;
import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;
import com.manager.email.application.port.in.SendVerificationEmailUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.generator.PlayerGenerator;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Player;
import org.junit.jupiter.api.BeforeEach;
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
class SignUpUserUseCaseTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

    @MockitoBean
    private SendVerificationEmailUseCase sendVerificationEmailUseCase;

    @Autowired
    private SignUpUserUseCase signUpUserUseCase;

    @Captor
    private ArgumentCaptor<User> captor;

    @BeforeEach
    void setUp() {
        clearInvocations(userRepository);
    }

    @Test
    void shouldSignUpUserFromPlayer() {
        Player player = PlayerGenerator.player().build();

        when(playerRepository.findById(player.id())).thenReturn(Optional.of(player));
        when(userRepository.existsByEmail(player.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        signUpUserUseCase.signupFromPlayer(player.id());

        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        User expectedUser = UserGenerator.user()
                .id(null)
                .email(player.email())
                .name(player.name())
                .surname(player.surname())
                .role(Role.PLAYER)
                .enabled(false)
                .createdAt(null)
                .updatedAt(null)
                .build();

        assertThat(savedUser).usingRecursiveComparison()
                .ignoringFields("verification")
                .isEqualTo(expectedUser);
        assertThat(savedUser.verification()).isNotNull();

        verify(sendVerificationEmailUseCase).sendVerificationEmail(
                eq(player.email()), eq(savedUser.verification().verificationCode()));
    }

    @Test
    void shouldFailWhenPlayerNotFound() {
        UUID nonExistentPlayerId = UUID.randomUUID();

        when(playerRepository.findById(nonExistentPlayerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> signUpUserUseCase.signupFromPlayer(nonExistentPlayerId))
                .isInstanceOf(PlayerNotFoundException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldFailWhenPlayerAlreadyAssignedToUser() {
        UUID existingUserId = UUID.randomUUID();
        Player player = PlayerGenerator.player().build().toBuilder().userId(existingUserId).build();

        when(playerRepository.findById(player.id())).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> signUpUserUseCase.signupFromPlayer(player.id()))
                .isInstanceOf(PlayerAlreadyAssignedToUserException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldFailWhenUserAlreadyExistsWithEmail() {
        Player player = PlayerGenerator.player().build();

        when(playerRepository.findById(player.id())).thenReturn(Optional.of(player));
        when(userRepository.existsByEmail(player.email())).thenReturn(true);

        assertThatThrownBy(() -> signUpUserUseCase.signupFromPlayer(player.id()))
                .isInstanceOf(UserAlreadyExists.class);

        verify(userRepository, never()).save(any());
    }
}
