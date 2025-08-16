package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreateUserRequestDTO;
import com.manager.payments.application.exception.UserNotFoundException;
import com.manager.payments.application.port.in.AssignPaymentToUserUseCase;
import com.manager.payments.application.port.in.CreateUserUseCase;
import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserRepository userRepository;
    private final AssignPaymentToUserUseCase assignPaymentToUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, UserRepository userRepository,
                          AssignPaymentToUserUseCase assignPaymentToUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.userRepository = userRepository;
        this.assignPaymentToUserUseCase = assignPaymentToUserUseCase;
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable("userId") UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @GetMapping("/user/{userId}/receipts")
    public List<ReceiptMinInfo> getUserReceipts(@PathVariable("userId") UUID userId) {
        return userRepository.findAllReceipts(userId);
    }

    @PostMapping("/user")
    public User createUser(@RequestBody CreateUserRequestDTO requestDTO) {
        return createUserUseCase.createUser(requestDTO);
    }

    @PutMapping("/user/{userId}/assign/{paymentId}")
    public User assignPaymentToUser(@PathVariable UUID userId, @PathVariable UUID paymentId) {
        return assignPaymentToUserUseCase.assignPaymentToUser(userId, paymentId);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userRepository.deleteById(userId);
    }
}
