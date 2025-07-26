package com.manager.payments.application.port.in;

import com.manager.payments.model.payments.Payment;

import java.util.UUID;

public interface FindPaymentUseCase {

    Payment findById(UUID id);

}
