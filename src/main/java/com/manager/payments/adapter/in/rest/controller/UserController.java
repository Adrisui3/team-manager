package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreateUserRequestDTO;
import com.manager.payments.application.port.in.CreateUserUseCase;
import com.manager.payments.model.users.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/user")
    public User createUser(@RequestBody CreateUserRequestDTO requestDTO) {
        return createUserUseCase.createUser(requestDTO);
    }
}
