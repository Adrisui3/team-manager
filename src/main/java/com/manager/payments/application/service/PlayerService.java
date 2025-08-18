package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.CreatePlayerRequestDTO;
import com.manager.payments.application.exception.PaymentNotFoundException;
import com.manager.payments.application.exception.PlayerAlreadyExistsException;
import com.manager.payments.application.exception.PlayerNotFoundException;
import com.manager.payments.application.port.in.AssignPaymentToUserUseCase;
import com.manager.payments.application.port.in.CreateUserUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.users.Player;
import com.manager.payments.model.users.PlayerMinInfo;
import com.manager.payments.model.users.PlayerStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService implements CreateUserUseCase, AssignPaymentToUserUseCase {

    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;

    public PlayerService(PaymentRepository paymentRepository,
                         PlayerRepository playerRepository) {
        this.paymentRepository = paymentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createUser(CreatePlayerRequestDTO requestDTO) {
        Optional<Player> existingPlayer =
                playerRepository.findByPersonalId(requestDTO.personalId());
        if (existingPlayer.isPresent()) {
            throw new PlayerAlreadyExistsException(requestDTO.personalId());
        }

        Player newPlayer = new Player(null, requestDTO.personalId(), requestDTO.name(), requestDTO.surname(),
                requestDTO.email(), requestDTO.birthDate(), requestDTO.category(), PlayerStatus.ENABLED,
                Collections.emptyList(), Collections.emptyList());
        return playerRepository.save(newPlayer);
    }

    @Override
    @Transactional
    public Player assignPaymentToPlayer(UUID playerId, UUID paymentId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        PlayerMinInfo playerMinInfo = PlayerMinInfo.from(player);

        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        PaymentMinInfo paymentMinInfo = PaymentMinInfo.from(payment);

        if (!player.payments().contains(paymentMinInfo)) {
            player.payments().add(paymentMinInfo);
            playerRepository.save(player);
        }

        if (!payment.users().contains(playerMinInfo)) {
            payment.users().add(playerMinInfo);
            paymentRepository.save(payment);
        }

        return player;
    }
}
