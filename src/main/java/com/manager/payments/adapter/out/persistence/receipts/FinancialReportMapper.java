package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.adapter.in.rest.dto.models.FinancialReportDto;
import com.manager.payments.model.reports.FinancialReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinancialReportMapper {

    FinancialReportDto toFinancialReportDto(FinancialReport financialReport);

}
