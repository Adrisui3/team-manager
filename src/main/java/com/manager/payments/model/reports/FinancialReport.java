package com.manager.payments.model.reports;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.receipts.ReceiptStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record FinancialReport(
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        Map<ReceiptStatus, BigDecimal> summaryByStatus,
        Map<Category, Map<ReceiptStatus, BigDecimal>> summaryByCategory,
        long averageDaysToPay,
        long averageDaysDelay
) {
}
