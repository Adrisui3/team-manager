package com.manager.payments.application.service;

import com.manager.payments.application.port.in.reports.GetFinancialReportUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import com.manager.payments.model.reports.FinancialReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService implements GetFinancialReportUseCase {

    private final ReceiptRepository receiptRepository;

    @Override
    public FinancialReport generateReport(LocalDate startDate, LocalDate endDate) {
        List<Receipt> receipts = receiptRepository.findAllExpiringBetween(startDate, endDate);
        List<Receipt> paidReceipts =
                receipts.stream().filter(receipt -> receipt.status() == ReceiptStatus.PAID).toList();

        BigDecimal totalAmount = receipts.stream().map(Receipt::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paidAmount = paidReceipts.stream().map(Receipt::amount).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<ReceiptStatus, BigDecimal> summaryByStatus = buildSummaryByStatus(receipts);
        Map<Category, Map<ReceiptStatus, BigDecimal>> summaryByCategory = buildSummaryByCategory(receipts);

        long averageDaysToPay = paidReceipts.stream()
                .mapToLong(receipt -> ChronoUnit.DAYS.between(receipt.issuedDate(), receipt.paymentDate())).sum();

        LocalDate currentDate = LocalDate.now();
        long averageDaysDelay =
                receipts.stream().filter(receipt -> receipt.status() == ReceiptStatus.OVERDUE)
                        .mapToLong(receipt -> ChronoUnit.DAYS.between(receipt.expiryDate(), currentDate)).sum();

        return FinancialReport.builder()
                .averageDaysToPay(averageDaysToPay)
                .averageDaysDelay(averageDaysDelay)
                .summaryByStatus(summaryByStatus)
                .summaryByCategory(summaryByCategory)
                .paidAmount(paidAmount)
                .totalAmount(totalAmount)
                .build();
    }

    private Map<ReceiptStatus, BigDecimal> buildSummaryByStatus(List<Receipt> receipts) {
        Map<ReceiptStatus, BigDecimal> sumByStatus = Arrays.stream(ReceiptStatus.values())
                .collect(Collectors.toMap(
                        status -> status,
                        status -> BigDecimal.ZERO
                ));

        sumByStatus.putAll(receipts.stream()
                .collect(Collectors.groupingBy(
                        Receipt::status,
                        Collectors.reducing(BigDecimal.ZERO, Receipt::amount, BigDecimal::add)
                )));

        return sumByStatus;
    }

    private Map<Category, Map<ReceiptStatus, BigDecimal>> buildSummaryByCategory(List<Receipt> receipts) {
        Map<Category, List<Receipt>> receiptsByCategory = receipts.stream()
                .collect(Collectors.groupingBy(r -> r.player().category()));

        return Arrays.stream(Category.values())
                .collect(Collectors.toMap(
                        category -> category,
                        category -> buildSummaryByStatus(
                                receiptsByCategory.getOrDefault(category, List.of())
                        )
                ));
    }
}
