package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaRepository;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaEntity;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaRepository;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.exception.UserNotFoundException;
import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final ReceiptMapper receiptMapper;
    private final ReceiptJpaRepository receiptJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(ReceiptMapper receiptMapper, ReceiptJpaRepository receiptJpaRepository,
                                 PaymentJpaRepository paymentJpaRepository,
                                 UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.receiptMapper = receiptMapper;
        this.receiptJpaRepository = receiptJpaRepository;
        this.paymentJpaRepository = paymentJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = userMapper.toUserJpaEntity(user, paymentJpaRepository, receiptJpaRepository);
        UserJpaEntity savedUserJpaEntity = userJpaRepository.save(userJpaEntity);
        return userMapper.toUser(savedUserJpaEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserJpaEntity> userJpaEntity = userJpaRepository.findById(id);
        return userJpaEntity.map(userMapper::toUser);
    }

    @Override
    public Optional<User> findByPersonalId(String personalId) {
        Optional<UserJpaEntity> userJpaEntity = userJpaRepository.findByPersonalId(personalId);
        return userJpaEntity.map(userMapper::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserJpaEntity> userJpaEntity = userJpaRepository.findByEmail(email);
        return userJpaEntity.map(userMapper::toUser);
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public Receipt addReceiptToUser(UUID userId, Receipt receipt) {
        UserJpaEntity userJpaEntity =
                userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ReceiptJpaEntity receiptJpaEntity = receiptMapper.toReceiptJpaEntity(receipt);

        userJpaEntity.getReceipts().add(receiptJpaEntity);
        receiptJpaEntity.setUser(userJpaEntity);
        UserJpaEntity savedUser = userJpaRepository.save(userJpaEntity);

        return receiptMapper.toReceipt(savedUser.getReceipts().getLast());
    }

    @Override
    public List<ReceiptMinInfo> findAllReceipts(UUID userId) {
        UserJpaEntity userJpaEntity =
                userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return userJpaEntity.getReceipts().stream().map(receiptMapper::toReceiptMinInfo).toList();
    }
}
