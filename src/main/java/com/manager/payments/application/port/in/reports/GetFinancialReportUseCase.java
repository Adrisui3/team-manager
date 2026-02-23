package com.manager.payments.application.port.in.reports;

import com.manager.payments.model.reports.FinancialReport;

import java.time.LocalDate;

public interface GetFinancialReportUseCase {

    FinancialReport generateReport(LocalDate startDate, LocalDate endDate);

}
