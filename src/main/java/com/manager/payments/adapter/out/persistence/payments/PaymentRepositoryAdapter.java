package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentMapper paymentMapper;
    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryAdapter(PaymentMapper paymentMapper,
                                    PaymentJpaRepository paymentJpaRepository) {
        this.paymentMapper = paymentMapper;
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        Page<PaymentJpaEntity> paymentJpaEntities = paymentJpaRepository.findAll(pageable);
        return paymentJpaEntities.map(paymentMapper::toPayment);
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = paymentMapper.toPaymentJpaEntity(payment);
        PaymentJpaEntity savedPaymentJpaEntity = paymentJpaRepository.save(paymentJpaEntity);
        return paymentMapper.toPayment(savedPaymentJpaEntity);
    }

    @Override
    public List<Payment> saveAll(List<Payment> payments) {
        List<PaymentJpaEntity> paymentJpaEntities =
                payments.stream().map(paymentMapper::toPaymentJpaEntity).toList();
        List<PaymentJpaEntity> savedPayments = paymentJpaRepository.saveAll(paymentJpaEntities);
        return savedPayments.stream().map(paymentMapper::toPayment).toList();
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        Optional<PaymentJpaEntity> paymentJpaEntity = paymentJpaRepository.findById(id);
        return paymentJpaEntity.map(paymentMapper::toPayment);
    }

    @Override
    public boolean existsById(UUID id) {
        return paymentJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return paymentJpaRepository.existsByCode(code);
    }

    @Override
    public void deleteById(UUID id) {
        if (!paymentJpaRepository.existsById(id))
            throw new PaymentNotFoundException(id);

        paymentJpaRepository.deleteById(id);
    }

    @Override
    public List<Payment> findAllActiveAndEndDateBefore(LocalDate date) {
        List<PaymentJpaEntity> payments = paymentJpaRepository.findAllByEndDateBeforeAndStatus(date,
                PaymentStatus.ACTIVE);
        return payments.stream().map(paymentMapper::toPayment).toList();
    }
}
