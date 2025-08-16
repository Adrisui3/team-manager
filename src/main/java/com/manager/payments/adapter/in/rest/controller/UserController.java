package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreateUserRequestDTO;
import com.manager.payments.application.port.in.AssignPaymentToUserUseCase;
import com.manager.payments.application.port.in.CreateUserUseCase;
import com.manager.payments.application.port.in.DeleteUserUseCase;
import com.manager.payments.application.port.in.FindUserUseCase;
import com.manager.payments.model.users.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private final FindUserUseCase findUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final AssignPaymentToUserUseCase assignPaymentToUserUseCase;

    public UserController(FindUserUseCase findUserUseCase, CreateUserUseCase createUserUseCase, DeleteUserUseCase deleteUserUseCase, AssignPaymentToUserUseCase assignPaymentToUserUseCase) {
        this.findUserUseCase = findUserUseCase;
        this.createUserUseCase = createUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.assignPaymentToUserUseCase = assignPaymentToUserUseCase;
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable("userId") UUID userId) {
        return findUserUseCase.findById(userId);
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
        deleteUserUseCase.deleteUserById(userId);
    }
}
