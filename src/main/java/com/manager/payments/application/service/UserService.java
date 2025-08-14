package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.CreateUserRequestDTO;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.application.exception.PaymentNotFoundException;
import com.manager.payments.application.exception.UserAlreadyExistsException;
import com.manager.payments.application.exception.UserNotFoundException;
import com.manager.payments.application.port.in.AssignPaymentToUserUseCase;
import com.manager.payments.application.port.in.CreateUserUseCase;
import com.manager.payments.application.port.in.DeleteUserUseCase;
import com.manager.payments.application.port.in.FindUserUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.users.User;
import com.manager.payments.model.users.UserMinInfo;
import com.manager.payments.model.users.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements CreateUserUseCase, AssignPaymentToUserUseCase, FindUserUseCase, DeleteUserUseCase {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public UserService(PaymentMapper paymentMapper, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserRequestDTO requestDTO) {
        Optional<User> existingUser = userRepository.findByPersonalId(requestDTO.personalId());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(requestDTO.personalId());
        }

        User newUser = new User(null, requestDTO.personalId(), requestDTO.name(), requestDTO.surname(), requestDTO.email(), requestDTO.birthDate(), requestDTO.category(), UserStatus.ENABLED, Collections.emptyList());
        return userRepository.save(newUser);
    }

    @Override
    @Transactional
    public User assignPaymentToUser(UUID userId, UUID paymentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        UserMinInfo userMinInfo = UserMinInfo.from(user);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        PaymentMinInfo paymentMinInfo = PaymentMinInfo.from(payment);

        if (!user.payments().contains(paymentMinInfo)) {
            user.payments().add(paymentMinInfo);
            userRepository.save(user);
        }

        if (!payment.users().contains(userMinInfo)) {
            payment.users().add(userMinInfo);
            paymentRepository.save(payment);
        }

        return user;
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }
}
