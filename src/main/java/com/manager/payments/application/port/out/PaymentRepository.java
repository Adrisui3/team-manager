package com.manager.payments.application.port.out;

import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Page<Payment> findAll(Pageable pageable);

    Payment save(Payment payment);

    List<Payment> saveAll(List<Payment> payments);

    Optional<Payment> findById(UUID id);

    boolean existsById(UUID id);

    boolean existsByCode(String code);

    void deleteById(UUID id);

    List<Payment> findAllExpired(LocalDate date);

    Page<Payment> findAll(String query, PaymentStatus status, Periodicity periodicity, Pageable pageable);
}
