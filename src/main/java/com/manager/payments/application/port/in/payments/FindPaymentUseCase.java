package com.manager.payments.application.port.in.payments;

import com.manager.payments.model.payments.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FindPaymentUseCase {

    Payment findById(UUID id);

    Page<Payment> findAll(String query, Pageable pageable);
}
