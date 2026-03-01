package com.manager.payments.application.service.payments;

import com.manager.payments.application.port.in.payments.FindPaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.payments.Periodicity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FindPaymentService implements FindPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;

    @Override
    public Payment findById(UUID id) {
        return paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public Page<Payment> findAll(String query, PaymentStatus status, Periodicity periodicity, Pageable pageable) {
        return paymentRepository.findAll(query.trim().toLowerCase(Locale.ROOT), status, periodicity, pageable);
    }

    @Override
    public Page<Payment> findAllAvailableForPlayer(UUID playerId, Pageable pageable) {
        if (!playerRepository.existsById(playerId))
            throw PlayerNotFoundException.byId(playerId);

        return paymentRepository.findAllAvailableForPlayer(playerId, pageable);
    }
}
