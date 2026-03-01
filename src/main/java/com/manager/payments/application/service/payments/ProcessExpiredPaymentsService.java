package com.manager.payments.application.service.payments;

import com.manager.payments.application.port.in.payments.ProcessExpiredPaymentsUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.payments.ExpiredPaymentProcessor;
import com.manager.payments.model.payments.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProcessExpiredPaymentsService implements ProcessExpiredPaymentsUseCase {

    private final PaymentRepository paymentRepository;

    @Override
    public void processExpiredPayments(LocalDate date) {
        List<Payment> expiredPayments = paymentRepository.findAllExpired(date);
        List<Payment> processedPaymens = ExpiredPaymentProcessor.processExpiredPayments(expiredPayments);
        log.info("{} payments have been marked as EXPIRED", processedPaymens.size());
        paymentRepository.saveAll(processedPaymens);
    }
}
