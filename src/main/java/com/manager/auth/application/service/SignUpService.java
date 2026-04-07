package com.manager.auth.application.service;

import com.manager.auth.adapter.in.rest.dto.requests.RegisterUserRequestDto;
import com.manager.auth.application.port.in.SignUpUserUseCase;
import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.exceptions.PlayerAlreadyAssignedToUserException;
import com.manager.auth.model.exceptions.UserAlreadyExists;
import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;
import com.manager.email.application.port.in.SendVerificationEmailUseCase;
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
public class SignUpService implements SignUpUserUseCase {

    private final UserRepository repository;
    private final PlayerRepository playerRepository;
    private final SendVerificationEmailUseCase verificationEmailUseCase;

    @Override
    public User signup(RegisterUserRequestDto request) {
        if (repository.existsByEmail(request.email()))
            throw new UserAlreadyExists(request.email());

        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .surname(request.surname())
                .role(request.role())
                .enabled(false)
                .build()
                .initializeVerification();

        verificationEmailUseCase.sendVerificationEmail(user.email(), user.verification().verificationCode());
        return repository.save(user);
    }

    @Override
    public User signupFromPlayer(UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> PlayerNotFoundException.byId(playerId));
        if (player.userId() != null) {
            throw new PlayerAlreadyAssignedToUserException(playerId, player.userId());
        }

        if (repository.existsByEmail(player.email())) {
            throw new UserAlreadyExists(player.email());
        }

        User user = User.builder()
                .email(player.email())
                .name(player.name())
                .surname(player.surname())
                .role(Role.PLAYER)
                .enabled(false)
                .build()
                .initializeVerification();
        User savedUser = repository.save(user);

        verificationEmailUseCase.sendVerificationEmail(user.email(), user.verification().verificationCode());
        playerRepository.save(player.toBuilder().userId(savedUser.id()).build());
        return savedUser;
    }
}
