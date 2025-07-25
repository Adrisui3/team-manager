package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.CreateUserRequestDTO;
import com.manager.payments.application.port.in.CreateUserUseCase;
import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.users.User;
import org.springframework.stereotype.Service;

@Service
public class UserService implements CreateUserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserRequestDTO requestDTO) {



        return null;
    }
}
