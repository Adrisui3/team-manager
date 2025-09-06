package com.manager.payments.application.port.in;

import java.time.LocalDate;

public interface ProcessOverdueReceiptsUseCase {

    void processOverdueReceipts(LocalDate date);

}
