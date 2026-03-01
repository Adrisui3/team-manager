package com.manager.payments.application.port.in.receipts;

import java.util.List;
import java.util.UUID;

public interface NotifyExpiredReceiptUseCase {

    void notifyExpiredReceipts(List<UUID> expiredReceiptsIds);

}
