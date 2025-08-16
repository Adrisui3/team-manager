package com.manager.payments.application.service;

import com.manager.payments.application.exception.PaymentNotAssignedException;
import com.manager.payments.application.exception.PaymentNotFoundException;
import com.manager.payments.application.exception.UserNotAssignedException;
import com.manager.payments.application.exception.UserNotFoundException;
import com.manager.payments.application.port.in.CreateReceiptUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.UserRepository;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.receipts.ReceiptStatus;
import com.manager.payments.model.users.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BillingService implements CreateReceiptUseCase {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public BillingService(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ReceiptMinInfo createReceipt(UUID userId, UUID paymentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if (!user.hasPayment(paymentId)) {
            throw new PaymentNotAssignedException(userId, paymentId);
        }

        if (!payment.hasUser(userId)) {
            throw new UserNotAssignedException(userId, paymentId);
        }

        // TODO: move to model
        LocalDate issuedDate = LocalDate.now();
        LocalDate expiryDate = issuedDate.plusDays(15);
        ReceiptMinInfo receipt = new ReceiptMinInfo(null, payment.amount(), issuedDate, null, expiryDate,
                ReceiptStatus.PENDING);

        user.receipts().add(receipt);
        User updatedUser = userRepository.save(user);
        return updatedUser.receipts().getLast();
    }
}
