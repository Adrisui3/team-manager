package com.manager.payments.application.port.out;

import com.manager.payments.model.payments.Payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    List<Payment> saveAll(List<Payment> payments);

    Optional<Payment> findById(UUID id);

    void deleteById(UUID id);

    List<Payment> findAllActiveAndEndDateBefore(LocalDate date);
}
