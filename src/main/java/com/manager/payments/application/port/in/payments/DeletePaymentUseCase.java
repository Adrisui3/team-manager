package com.manager.payments.application.port.in.payments;

import java.util.UUID;

public interface DeletePaymentUseCase {

    void deleteById(UUID id);
}
