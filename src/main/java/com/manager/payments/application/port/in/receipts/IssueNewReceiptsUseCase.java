package com.manager.payments.application.port.in.receipts;

import java.time.LocalDate;

public interface IssueNewReceiptsUseCase {
    void issueNewReceipts(LocalDate date);
}
