package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.models.FinancialReportDto;
import com.manager.payments.adapter.out.persistence.receipts.FinancialReportMapper;
import com.manager.payments.application.port.in.reports.GetFinancialReportUseCase;
import com.manager.payments.model.reports.FinancialReport;
import com.manager.shared.response.ErrorResponse;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "Financial reports", description = "Financial reports management endpoints")
@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final GetFinancialReportUseCase getFinancialReportUseCase;
    private final FinancialReportMapper mapper;

    @Operation(summary = "Obtain financial report for a given interval")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Financial report", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Start date cannot be after the end date", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping
    public ResponseEntity<ResponseDto<FinancialReportDto>> getFinancialReport(@RequestParam(name = "startDate") LocalDate startDate,
                                                                              @RequestParam(name = "endDate") LocalDate endDate) {
        FinancialReport report = getFinancialReportUseCase.generateReport(startDate, endDate);
        return ResponseEntity.ok(new ResponseDto<>(mapper.toFinancialReportDto(report)));
    }

}
