package com.manager.payments.application.port.in.payments;

import java.time.LocalDate;

public interface ProcessExpiredPaymentsUseCase {

    void processExpiredPayments(LocalDate date);

}
