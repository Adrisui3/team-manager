package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.payments.Payment;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentMapper paymentMapper;
    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryAdapter(PaymentMapper paymentMapper, PaymentJpaRepository paymentJpaRepository) {
        this.paymentMapper = paymentMapper;
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = paymentMapper.toPaymentJpaEntity(payment);
        PaymentJpaEntity savedPaymentJpaEntity = paymentJpaRepository.save(paymentJpaEntity);
        return paymentMapper.toPayment(savedPaymentJpaEntity);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        Optional<PaymentJpaEntity> paymentJpaEntity = paymentJpaRepository.findById(id);
        return paymentJpaEntity.map(paymentMapper::toPayment);
    }
}
