package com.manager.payments.adapter.in.rest.dto.models;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.receipts.ReceiptStatus;

import java.math.BigDecimal;
import java.util.Map;

public record FinancialReportDto(
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        Map<ReceiptStatus, BigDecimal> summaryByStatus,
        Map<Category, Map<ReceiptStatus, BigDecimal>> summaryByCategory,
        double averageDaysToPay,
        double averageDaysDelay) {
}
