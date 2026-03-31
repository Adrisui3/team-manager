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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SignUpUserUseCaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @MockitoBean
    private SendVerificationEmailUseCase sendVerificationEmailUseCase;

    @Autowired
    private SignUpUserUseCase signUpUserUseCase;

    @Test
    void shouldSignUpUserFromPlayer() {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());

        User result = signUpUserUseCase.signupFromPlayer(player.id());

        User expectedUser = UserGenerator.user()
                .email(player.email())
                .name(player.name())
                .surname(player.surname())
                .role(Role.PLAYER)
                .enabled(false)
                .build();

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id", "verification", "createdAt", "updatedAt")
                .isEqualTo(expectedUser);
        assertThat(result.verification()).isNotNull();

        assertThat(userRepository.findByEmail(player.email())).hasValueSatisfying(u ->
                assertThat(u).usingRecursiveComparison()
                        .ignoringFields("id", "verification", "createdAt", "updatedAt")
                        .isEqualTo(expectedUser));

        verify(sendVerificationEmailUseCase).sendVerificationEmail(
                result.email(), result.verification().verificationCode());
    }

    @Test
    void shouldFailWhenPlayerNotFound() {
        UUID nonExistentPlayerId = UUID.randomUUID();

        assertThatThrownBy(() -> signUpUserUseCase.signupFromPlayer(nonExistentPlayerId))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    void shouldFailWhenPlayerAlreadyAssignedToUser() {
        User user = userRepository.save(UserGenerator.user().id(null).build());
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        playerRepository.save(player.toBuilder().userId(user.id()).build());

        assertThatThrownBy(() -> signUpUserUseCase.signupFromPlayer(player.id()))
                .isInstanceOf(PlayerAlreadyAssignedToUserException.class);
    }

    @Test
    void shouldFailWhenUserAlreadyExistsWithEmail() {
        String sharedEmail = "shared@test.com";
        Player player = playerRepository.save(PlayerGenerator.player().id(null).email(sharedEmail).build());
        userRepository.save(UserGenerator.user().id(null).email(sharedEmail).build());

        assertThatThrownBy(() -> signUpUserUseCase.signupFromPlayer(player.id()))
                .isInstanceOf(UserAlreadyExists.class);
    }
}
