package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.CreateUserRequestDTO;
import com.manager.payments.application.exception.UserAlreadyExistsException;
import com.manager.payments.application.port.in.CreateUserUseCase;
import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.users.User;
import com.manager.payments.model.users.UserStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements CreateUserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserRequestDTO requestDTO) {
        Optional<User> existingUser = userRepository.findByPersonalId(requestDTO.personalId());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(requestDTO.personalId());
        }

        User newUser = new User(null, requestDTO.personalId(), requestDTO.name(), requestDTO.surname(), requestDTO.email(), requestDTO.birthDate(), requestDTO.category(), UserStatus.ENABLED);
        return userRepository.save(newUser);
    }
}
