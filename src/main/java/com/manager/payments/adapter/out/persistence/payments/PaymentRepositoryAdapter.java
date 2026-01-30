package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentMapper mapper;
    private final PaymentJpaRepository repository;

    public PaymentRepositoryAdapter(PaymentMapper mapper,
                                    PaymentJpaRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        Page<PaymentJpaEntity> paymentJpaEntities = repository.findAll(pageable);
        return paymentJpaEntities.map(mapper::toPayment);
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = mapper.toPaymentJpaEntity(payment);
        PaymentJpaEntity savedPaymentJpaEntity = repository.save(paymentJpaEntity);
        return mapper.toPayment(savedPaymentJpaEntity);
    }

    @Override
    public List<Payment> saveAll(List<Payment> payments) {
        List<PaymentJpaEntity> paymentJpaEntities =
                payments.stream().map(mapper::toPaymentJpaEntity).toList();
        List<PaymentJpaEntity> savedPayments = repository.saveAll(paymentJpaEntities);
        return savedPayments.stream().map(mapper::toPayment).toList();
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        Optional<PaymentJpaEntity> paymentJpaEntity = repository.findById(id);
        return paymentJpaEntity.map(mapper::toPayment);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Payment> findAllExpired(LocalDate date) {
        List<PaymentJpaEntity> payments = repository.findAllExpired(date);
        return payments.stream().map(mapper::toPayment).toList();
    }

    @Override
    public Page<Payment> findAll(String query, PaymentStatus status, Periodicity periodicity, Pageable pageable) {
        Page<PaymentJpaEntity> payments = repository.findAll(query, status, periodicity, pageable);
        return payments.map(mapper::toPayment);
    }
}
