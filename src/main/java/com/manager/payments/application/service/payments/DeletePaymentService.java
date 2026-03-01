package com.manager.payments.application.service.payments;

import com.manager.payments.application.port.in.payments.DeletePaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePaymentService implements DeletePaymentUseCase {

    private final PaymentRepository paymentRepository;

    @Override
    public void deleteById(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException(id);
        }

        paymentRepository.deleteById(id);
    }
}
