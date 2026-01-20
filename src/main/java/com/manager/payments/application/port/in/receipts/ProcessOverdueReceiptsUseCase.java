package com.manager.payments.application.port.in.receipts;

import java.time.LocalDate;

public interface ProcessOverdueReceiptsUseCase {

    void processOverdueReceipts(LocalDate date);

}
