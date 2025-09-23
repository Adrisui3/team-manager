package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreatePlayerRequestDTO;
import com.manager.payments.application.port.in.AssignPaymentToPlayerUseCase;
import com.manager.payments.application.port.in.CreatePlayerUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.players.Player;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final CreatePlayerUseCase createPlayerUseCase;
    private final PlayerRepository playerRepository;
    private final AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase;

    public PlayerController(CreatePlayerUseCase createPlayerUseCase, PlayerRepository playerRepository,
                            AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase) {
        this.createPlayerUseCase = createPlayerUseCase;
        this.playerRepository = playerRepository;
        this.assignPaymentToPlayerUseCase = assignPaymentToPlayerUseCase;
    }

    @GetMapping("/{playerId}")
    public Player getUser(@PathVariable("playerId") UUID playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    @GetMapping("/{playerId}/receipts")
    public List<ReceiptMinInfo> getUserReceipts(@PathVariable("playerId") UUID playerId) {
        return playerRepository.findAllReceipts(playerId);
    }

    @PostMapping
    public Player createUser(@RequestBody CreatePlayerRequestDTO requestDTO) {
        return createPlayerUseCase.createPlayer(requestDTO);
    }

    @PutMapping("/{playerId}/assign/{paymentId}")
    public Player assignPaymentToPlayer(@PathVariable UUID playerId, @PathVariable UUID paymentId) {
        return assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);
    }

    @DeleteMapping("/{playerId}")
    public void deleteUser(@PathVariable UUID playerId) {
        playerRepository.deleteById(playerId);
    }
}
