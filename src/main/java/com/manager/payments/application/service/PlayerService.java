package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.CreatePlayerRequestDTO;
import com.manager.payments.application.port.in.AssignPaymentToPlayerUseCase;
import com.manager.payments.application.port.in.CreatePlayerUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.exceptions.PlayerAlreadyExistsException;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerMinInfo;
import com.manager.payments.model.players.PlayerStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService implements CreatePlayerUseCase, AssignPaymentToPlayerUseCase {

    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;

    public PlayerService(PaymentRepository paymentRepository,
                         PlayerRepository playerRepository) {
        this.paymentRepository = paymentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createPlayer(CreatePlayerRequestDTO requestDTO) {
        Optional<Player> existingPlayer =
                playerRepository.findByPersonalId(requestDTO.personalId());
        if (existingPlayer.isPresent()) {
            throw new PlayerAlreadyExistsException(requestDTO.personalId());
        }

        Player newPlayer = new Player(UUID.randomUUID(), requestDTO.personalId(), requestDTO.name(),
                requestDTO.surname(), requestDTO.email(), requestDTO.birthDate(), requestDTO.category(),
                PlayerStatus.ENABLED, Collections.emptyList(), Collections.emptyList());
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

        if (!player.hasPayment(paymentId) && !payment.hasPlayer(playerId)) {
            player.payments().add(paymentMinInfo);
            payment.players().add(playerMinInfo);

            if (payment.status().equals(PaymentStatus.ACTIVE)) {
                player.createReceiptFor(payment);
            }

            playerRepository.save(player);
            paymentRepository.save(payment);
        }

        return player;
    }
}
