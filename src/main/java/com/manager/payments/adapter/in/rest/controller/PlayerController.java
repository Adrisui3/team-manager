package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreatePlayerRequestDTO;
import com.manager.payments.application.exception.PlayerNotFoundException;
import com.manager.payments.application.port.in.AssignPaymentToUserUseCase;
import com.manager.payments.application.port.in.CreateUserUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.Player;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PlayerController {

    private final CreateUserUseCase createUserUseCase;
    private final PlayerRepository playerRepository;
    private final AssignPaymentToUserUseCase assignPaymentToUserUseCase;

    public PlayerController(CreateUserUseCase createUserUseCase, PlayerRepository playerRepository,
                            AssignPaymentToUserUseCase assignPaymentToUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.playerRepository = playerRepository;
        this.assignPaymentToUserUseCase = assignPaymentToUserUseCase;
    }

    @GetMapping("/user/{userId}")
    public Player getUser(@PathVariable("userId") UUID userId) {
        return playerRepository.findById(userId).orElseThrow(() -> new PlayerNotFoundException(userId));
    }

    @GetMapping("/user/{userId}/receipts")
    public List<ReceiptMinInfo> getUserReceipts(@PathVariable("userId") UUID userId) {
        return playerRepository.findAllReceipts(userId);
    }

    @PostMapping("/user")
    public Player createUser(@RequestBody CreatePlayerRequestDTO requestDTO) {
        return createUserUseCase.createUser(requestDTO);
    }

    @PutMapping("/user/{userId}/assign/{paymentId}")
    public Player assignPaymentToUser(@PathVariable UUID userId, @PathVariable UUID paymentId) {
        return assignPaymentToUserUseCase.assignPaymentToPlayer(userId, paymentId);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        playerRepository.deleteById(userId);
    }
}
