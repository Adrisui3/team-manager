package com.manager.payments.application.port.out;

import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByPersonalId(String personalId);

    Optional<User> findByEmail(String email);

    void deleteById(UUID id);

    Receipt addReceiptToUser(UUID user, Receipt receipt);

    List<ReceiptMinInfo> findAllReceipts(UUID userId);
}
